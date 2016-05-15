package com.bountive.dystopia.world.component;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import com.bountive.dystopia.debug.logger.LoggerUtil;
import com.bountive.dystopia.file.FileResourceLocation;
import com.bountive.dystopia.file.FileResourceLocation.EnumFileExtension;
import com.bountive.dystopia.file.setting.ProgramSettings;
import com.bountive.dystopia.world.component.Chunk.EnumSafetyLevel;

public class ChunkLoader {

	public ArrayList<Chunk> loadedChunks;
	private ArrayList<Chunk> removedChunks;
	
	public ChunkLoader() {
		loadedChunks = new ArrayList<>(64);
		removedChunks = new ArrayList<>();
	}
	
	public ArrayList<Chunk> cleanQueue(WorldChunkManager manager, ChunkSaver chunkSaver, EnumQuadrant quadrant, int chunkX, int chunkZ) {
		//Step1. Get the render distance.
		int renderDistance = ProgramSettings.getCustomRenderDistance();
		removedChunks.clear();
		
		//Step2. For all the chunks in loadedChunks, check if they are in range from the current chunk passed in.
		for (int i = 0; i < loadedChunks.size(); i++) {
			Chunk c = loadedChunks.get(i);
			
			//Step3. If a chunk is not in range, remove it from the loaded chunks list.
			if (c.getRadialDistance(quadrant, chunkX, chunkZ) > renderDistance) {
				//Step4. Add the removed chunk to removedChunks a list.
				removedChunks.add(c);
				loadedChunks.remove(i);
//				LoggerUtil.logInfo(getClass(), "Removing Chunk: (" + c.getChunkX() + ", " + c.getChunkZ() + ") from index: " + i);
				i--;
			}
		}
		
		//Step5. Save the removed chunks to the save file.
		chunkSaver.saveChunks(manager, removedChunks);
		
		//Step6. Return removedChunks.
		return removedChunks;
	}
	
	public void loadRequiredChunks(WorldChunkManager manager, EnumQuadrant currentQuadrant, int currentChunkX, int currentChunkZ) {
		//Step1. Get the render distance.
		int radius = ProgramSettings.getCustomRenderDistance();
		
		//Step2. Get center chunk.
		//Step3. Get the bottom left chunk for for-loop.
		
		for (int radiusIndex = 0, length = 1; radiusIndex <= radius; radiusIndex++, length+=2) {
			int startingChunkX = currentChunkX - radiusIndex;
			int startingChunkZ = currentChunkZ - radiusIndex;
			
			for (int z = 0; z < length; z++) {
				int newChunkZ = startingChunkZ + z;
				
				for (int x = 0; x < length; x++) {
					int newChunkX = startingChunkX + x;
					
					EnumQuadrant newQuadrant = EnumQuadrant.getQuadrant(newChunkX, newChunkZ);
					
					//If the file does not exist create a new chunk.
					if (!isChunkLoaded(newQuadrant, newChunkX, newChunkZ)) {
						if (!isChunkInFile(manager, newQuadrant, newChunkX, newChunkZ)) {
							//Create a new random chunk from the world seed.
							LoggerUtil.logInfo(getClass(), "Adding new chunk to chunkLoader: (" + newChunkX + ", " + newChunkZ + ")");
							Chunk newChunk = new Chunk(newChunkX, newChunkZ);
							loadedChunks.add(newChunk);
						}
					}
				}
			}
		}
	}
	
	private boolean isChunkLoaded(EnumQuadrant newQuadrant, int newChunkX, int newChunkZ) {
		for (Chunk c : loadedChunks) {
//			System.out.println(c.getChunkX() + " " + c.getChunkZ() + " | " + newChunkX + " " + newChunkZ);
			if (c.isChunk(newQuadrant, newChunkX, newChunkZ)) {
//				LoggerUtil.logInfo(getClass(), "Chunk is already loaded: Quadrant: " + c.getQuadrant() + "(" + newChunkX + ", " + newChunkZ + ")");
				return true;
			}
		}
		return false;
	}
	
	private boolean isChunkInFile(WorldChunkManager manager, EnumQuadrant newQuadrant, int newChunkX, int newChunkZ) {
		return loadedChunkFromFile(manager, newQuadrant, newChunkX, newChunkZ);
	}
	
	public boolean loadedChunkFromFile(WorldChunkManager manager, EnumQuadrant quadrant, int chunkX, int chunkZ) {
		FileResourceLocation chunkFile = new FileResourceLocation(manager.LEVEL, quadrant.toString(), EnumFileExtension.DAT);
		File location = new File(chunkFile.getFullPath());
		
		if (location.exists()) {
			Chunk newChunk = null;
			
			try (DataInputStream is = new DataInputStream(new FileInputStream(location));) {
				//Open file and traverse through it and compare chunkX and chunkZ values.
				//If file chunkX and Z values == chunkX and Z values then load up chunk from that position in file.
				
				//TODO: TEST IF location.length() is divisible by CHUNK_BYTE_LENGTH because it should always be.
				int chunkAmount = (int)(location.length() / manager.CHUNK_BYTE_LENGTH);
				
				for (int i = 0; i < chunkAmount; i++) {
					int fileChunkX = is.readInt();
					int fileChunkZ = is.readInt();
					
					if (fileChunkX == chunkX && fileChunkZ == chunkZ) {
						//Continue to read in chunk data.
						EnumSafetyLevel fileLevel = EnumSafetyLevel.values()[is.readByte()];
						byte[][] fileTiles = new byte[Chunk.CHUNK_SIZE][Chunk.CHUNK_SIZE];
						
						for (int z = 0; z < Chunk.CHUNK_SIZE; z++) {
							for (int x = 0; x < Chunk.CHUNK_SIZE; x++) {
								fileTiles[x][z] = is.readByte();
							}
						}
						newChunk = new Chunk(fileChunkX, fileChunkZ, fileLevel, fileTiles);
						LoggerUtil.logInfo(getClass(), "Adding file chunk to chunkLoader: (" + newChunk.getChunkX() + ", " + newChunk.getChunkZ() + ")");
						loadedChunks.add(newChunk);
						
//						LoggerUtil.logInfo(getClass(), "Successfully loaded chunk from file.");
						return true;
					}
					
					if (i != chunkAmount - 1) {
						is.skip(manager.CHUNK_BYTE_LENGTH - (2 * Integer.BYTES));
					}
				}
			} catch (FileNotFoundException e) {
				if (chunkFile.getFullPath() != null) {
					LoggerUtil.logError(getClass(), "File: " + chunkFile.getFullPath() + " could not be found.", e);
				}
				else {
					LoggerUtil.logError(getClass(), "File path is null.", e);
				}
			} catch (EOFException e) {
				LoggerUtil.logError(getClass(), "DataInputStream has reached the end of the file.", e);
			} catch (IOException e) {
				LoggerUtil.logError(getClass(), e);
			}
		}
		
		return false;
		
//		if (location.exists()) {
//			Chunk newChunk = null;
//			
//			try (DataInputStream is = new DataInputStream(new FileInputStream(location));) {
//				int newChunkX = is.readInt();
//				int newChunkZ = is.readInt();
//				EnumSafetyLevel newLevel = EnumSafetyLevel.values()[is.readByte()];
//				byte[][] tiles = new byte[Chunk.CHUNK_SIZE][Chunk.CHUNK_SIZE];
//				
//				for (int z = 0; z < Chunk.CHUNK_SIZE; z++) {
//					for (int x = 0; x < Chunk.CHUNK_SIZE; x++) {
//						tiles[x][z] = is.readByte();
//					}
//				}
//				newChunk = new Chunk(newChunkX, newChunkZ, newLevel, tiles);
//				LoggerUtil.logInfo(getClass(), "Adding old chunk to chunkLoader: (" + newChunk.getChunkX() + ", " + newChunk.getChunkZ() + ")");
//				loadedChunks.add(newChunk);
//				
//			} catch (FileNotFoundException e) {
//				if (chunkFile != null) {
//					LoggerUtil.logError(getClass(), "File: " + chunkFile.getFullPath() + " could not be found.", e);
//				}
//				else {
//					LoggerUtil.logError(getClass(), "File path is null.", e);
//				}
//			} catch (EOFException e) {
//				LoggerUtil.logError(getClass(), "DataInputStream has reached the end of the file.", e);
//			} catch (IOException e) {
//				LoggerUtil.logError(getClass(), e);
//			}
//			
//			LoggerUtil.logInfo(getClass(), "Loaded chunk from file.");
//			return true;
//		}
//		else {
//			return false;
//		}
	}
}
