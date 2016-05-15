package com.bountive.dystopia.world.component;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import com.bountive.dystopia.debug.logger.LoggerUtil;
import com.bountive.dystopia.file.FileResourceLocation;
import com.bountive.dystopia.file.FileResourceLocation.EnumFileExtension;

public class ChunkSaver {
	
//	public final ResourceDirectory LEVEL;
//	public final int CHUNK_BYTE_LENGTH;
	
//	public final ResourceDirectory QUAD_1;
//	public final ResourceDirectory QUAD_2;
//	public final ResourceDirectory QUAD_3;
//	public final ResourceDirectory QUAD_4;
	
	public ChunkSaver() {
//		LEVEL = new ResourceDirectory(worldDirectory.getFullDirectory(), "level", false);
//		CHUNK_BYTE_LENGTH = (2 * Integer.BYTES) + 1 + Chunk.CHUNK_SIZE * Chunk.CHUNK_SIZE;
		
//		QUAD_1 = new ResourceDirectory(worldDirectory.getFullDirectory(), "quad1", false);
//		QUAD_2 = new ResourceDirectory(worldDirectory.getFullDirectory(), "quad2", false);
//		QUAD_3 = new ResourceDirectory(worldDirectory.getFullDirectory(), "quad3", false);
//		QUAD_4 = new ResourceDirectory(worldDirectory.getFullDirectory(), "quad4", false);
	}
	
	public void saveChunks(WorldChunkManager manager, ArrayList<Chunk> chunks) {
		for (Chunk c : chunks) {
			saveChunk(manager, c);
//			EnumQuadrant quadrant = EnumQuadrant.getQuadrant(c.getChunkX(), c.getChunkZ());
//			
//			switch (quadrant) {
//			case QUADRANT_1: {
//				saveChunk(QUAD_1, c);
//				break;
//			}
//			case QUADRANT_2: {
//				saveChunk(QUAD_2, c);
//				break;
//			}
//			case QUADRANT_3: {
//				saveChunk(QUAD_3, c);
//				break;
//			}
//			default: {
//				saveChunk(QUAD_4, c);
//			}
//			}
		}
	}
	
	private synchronized void saveChunk(WorldChunkManager manager, Chunk c) {
		EnumQuadrant quadrant = EnumQuadrant.getQuadrant(c.getChunkX(), c.getChunkZ());
		
		FileResourceLocation quadrantFile = new FileResourceLocation(manager.LEVEL, quadrant.toString(), EnumFileExtension.DAT);

		File location = new File(quadrantFile.getFullPath());
		
		if (!location.exists()) {
			new File(quadrantFile.getParentDirectory().getFullDirectory()).mkdirs();
			System.out.println("MADE LOCATION DIR: " + location.getAbsolutePath());
			
			//Add in chunk at the beginning of the file because we know the file is empty.
			try (DataOutputStream os = new DataOutputStream(new FileOutputStream(location));) {
				os.writeInt(c.getChunkX());
				os.writeInt(c.getChunkZ());
				os.writeByte(c.getSafetyLevel().ordinal());
				
				for (int z = 0; z < Chunk.CHUNK_SIZE; z++) {
					for (int x = 0; x < Chunk.CHUNK_SIZE; x++) {
						os.writeByte(c.getTileID(x, z));
					}
				}
				LoggerUtil.logInfo(getClass(), "Chunk successfully saved.");
			} catch (FileNotFoundException e) {
				if (quadrantFile.getFullPath() != null) {
					LoggerUtil.logError(getClass(), "File: " + quadrantFile.getFullPath() + " could not be found.", e);
				}
				else {
					LoggerUtil.logError(getClass(), "File path is null.", e);
				}
			} catch (IOException e) {
				LoggerUtil.logError(getClass(), e);
			}
		}
		else {
			//Create a DataInputStream.
			int offset = 0;
			byte[] copiedFile = new byte[(int)location.length()];
			
			try {
				try (DataInputStream is = new DataInputStream(new FileInputStream(location));) {
					
					//The amount of times to offset by chunk-byte-length in the file.
					
					//Read the whole file once and copy it into an array.
					for (int i = 0; i < copiedFile.length; i++) {
						copiedFile[i] = is.readByte();
					}
				}
				
				try (DataInputStream is = new DataInputStream(new FileInputStream(location));) {
					
					//Read in the chunkX and chunkZ bytes and then offset the chunks by CHUNK_BYTE_SIZE.
					while(is.available() > 0) {
						
						int fileChunkX = is.readInt();
						int fileChunkZ = is.readInt();
						
//						System.out.println("File Chunk: (" + fileChunkX + ", " + fileChunkZ + ") | Game Chunk: ("+ c.getChunkX() + ", " + c.getChunkZ() + ")");
						
						if (fileChunkX == c.getChunkX() && fileChunkZ == c.getChunkZ()) {
							updateLevelFile(manager, location, copiedFile, c, offset, true);
							return;
						}
						else if ((c.getChunkX() < fileChunkX) || (c.getChunkX() == fileChunkX && c.getChunkZ() < fileChunkZ)) {
							updateLevelFile(manager, location, copiedFile, c, offset, false);
							return;
						}
						
						//Skip the chunk data - 2 integer-byte-lengths because we read the x and z coords from the previous chunk.
						is.skip(manager.CHUNK_BYTE_LENGTH - (2 * Integer.BYTES));
						offset++;
					}
					
//					System.out.println("HELL FUCKING LO");
					updateLevelFile(manager, location, copiedFile, c, offset, false);
				}
			}	
			catch (FileNotFoundException e) {
				if (quadrantFile.getFullPath() != null) {
					LoggerUtil.logError(getClass(), "File: " + quadrantFile.getFullPath() + " could not be found.", e);
				}
				else {
					LoggerUtil.logError(getClass(), "File path is null.", e);
				}
			} catch (IOException e) {
				LoggerUtil.logError(getClass(), e);
			}
		}
		
//		try (DataOutputStream os = new DataOutputStream(new FileOutputStream(location));) {
//			
//			os.writeInt(c.getChunkX());
//			os.writeInt(c.getChunkZ());
//			os.writeByte(c.getSafetyLevel().ordinal());
//			
//			for (int z = 0; z < Chunk.CHUNK_SIZE; z++) {
//				for (int x = 0; x < Chunk.CHUNK_SIZE; x++) {
//					os.writeByte(c.getTileID(x, z));
//				}
//			}
//			LoggerUtil.logInfo(getClass(), "Chunk successfully saved.");
//		} catch (FileNotFoundException e) {
//			if (chunkFile != null) {
//				LoggerUtil.logError(getClass(), "File: " + chunkFile.getFullPath() + " could not be found.", e);
//			}
//			else {
//				LoggerUtil.logError(getClass(), "File path is null.", e);
//			}
//		} catch (IOException e) {
//			LoggerUtil.logError(getClass(), e);
//		}
	}
	
	//Add all of the bytes being read into a list to keep for copying.
	
	//If the previous chunkX and chunkZ are the right values to put the next chunk in,
	//Copy the rest of the bytes from this point to the end of the file.
	//then take the first copied list and add it to a new file.
	//Add in the new chunk info.
	//Add in the second copied list.
	//Delete the old file and replace with new one.
	private void updateLevelFile(WorldChunkManager manager, File levelFile, byte[] copiedFile, Chunk addedChunk, int offset, boolean overrideChunk) throws FileNotFoundException, IOException {
//		LoggerUtil.logInfo(getClass(), "Updating Level file!");
		try (DataOutputStream os = new DataOutputStream(new FileOutputStream(levelFile));) {
			
			//Gets the amount of bytes to offset to add the new chunk.
			int byteOffset = offset * manager.CHUNK_BYTE_LENGTH;
			for (int i = 0; i < byteOffset; i++) {
				//Write the bytes in file from old file.
				os.writeByte(copiedFile[i]);
			}
			
			//Write the new chunk after the old bytes.
			os.writeInt(addedChunk.getChunkX());
			os.writeInt(addedChunk.getChunkZ());
			os.writeByte(addedChunk.getSafetyLevel().ordinal());
			
			for (int z = 0; z < Chunk.CHUNK_SIZE; z++) {
				for (int x = 0; x < Chunk.CHUNK_SIZE; x++) {
					os.writeByte(addedChunk.getTileID(x, z));
				}
			}
			
			//Offset the byteOffset by a chunk-byte-length because we just added another chunk
			if (overrideChunk) {
				byteOffset += manager.CHUNK_BYTE_LENGTH;
			}
			//Finish adding in the old bytes.
			for (int i = byteOffset; i < copiedFile.length; i++) {
				os.writeByte(copiedFile[i]);
			}
		}
		LoggerUtil.logInfo(getClass(), "Chunk (" + addedChunk.getChunkX() + ", " + addedChunk.getChunkZ() + ") successfully saved.");
	}
}
