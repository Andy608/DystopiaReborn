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
import com.bountive.dystopia.file.ResourceDirectory;
import com.bountive.dystopia.file.setting.ProgramSettings;
import com.bountive.dystopia.world.component.Chunk.EnumSafetyLevel;

public class ChunkLoader {

	protected ArrayList<Chunk> loadedChunks;
	ArrayList<Chunk> removedChunks;
	
	public ChunkLoader() {
		loadedChunks = new ArrayList<>(64);
		removedChunks = new ArrayList<>();
	}
	
	public ArrayList<Chunk> cleanQueue(ChunkSaver chunkSaver, EnumQuadrant quadrant, int chunkX, int chunkZ) {
		//Step1. Get the render distance.
		int renderDistance = ProgramSettings.getCustomRenderDistance();
		//Step2. For all the chunks in loadedChunks, check if they are in range from the current chunk passed in.
		removedChunks.clear();
		
//		for (Chunk c : loadedChunks) {
//			removedChunks.add(c);
//		}
		
//		loadedChunks.clear();
		for (int i = 0; i < loadedChunks.size(); i++) {
			Chunk c = loadedChunks.get(i);
			
			//Step3. If a chunk is not in range, remove it from the loaded chunks list.
			if (c.getRadialDistance(quadrant, chunkX, chunkZ) > renderDistance) {
				//Step4. Add the removed chunk to removedChunks a list.
				removedChunks.add(c);
				loadedChunks.remove(i);
				LoggerUtil.logInfo(getClass(), "Removing Chunk: (" + c.getChunkX() + ", " + c.getChunkZ() + ") from index: " + i);
				i--;
				//TODO: TEST
			}
		}
		
		//Step5. Save the removed chunks to the save file.
		
		//TODO: FIX THIS
		chunkSaver.saveChunks(removedChunks);
		//Saturday: Save chunks in world file!
		//Glitch where it is saving the chunks into folders.
		
		
		
		//Step6. Return removedChunks.
		return removedChunks;
	}
	
	public void loadRequiredChunks(ChunkSaver chunkSaver, EnumQuadrant currentQuadrant, int currentChunkX, int currentChunkZ) {
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
						if (!isChunkInFile(chunkSaver, newQuadrant, newChunkX, newChunkZ)) {
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
	
	private boolean isChunkInFile(ChunkSaver chunkSaver, EnumQuadrant newQuadrant, int newChunkX, int newChunkZ) {
		switch (newQuadrant) {
		case QUADRANT_1: {
			return loadedChunkFromFile(chunkSaver.QUAD_1, newChunkX, newChunkZ);
		}
		case QUADRANT_2: {
			return loadedChunkFromFile(chunkSaver.QUAD_2, newChunkX, newChunkZ);
		}
		case QUADRANT_3: {
			return loadedChunkFromFile(chunkSaver.QUAD_3, newChunkX, newChunkZ);
		}
		default: {
			return loadedChunkFromFile(chunkSaver.QUAD_4, newChunkX, newChunkZ);
		}
		}
	}
	
	public boolean loadedChunkFromFile(ResourceDirectory dir, int chunkX, int chunkZ) {
		FileResourceLocation chunkFile = new FileResourceLocation(dir, chunkX + "_" + chunkZ + "_chunk", EnumFileExtension.DAT);
		File location = new File(chunkFile.getFullPath());
		
		if (location.exists()) {
			Chunk newChunk = null;
			
			try (DataInputStream is = new DataInputStream(new FileInputStream(location));) {
				int newChunkX = is.readByte();
				int newChunkZ = is.readByte();
				EnumSafetyLevel newLevel = EnumSafetyLevel.values()[is.readByte()];
				byte[][] tiles = new byte[Chunk.CHUNK_SIZE][Chunk.CHUNK_SIZE];
				
				for (int z = 0; z < Chunk.CHUNK_SIZE; z++) {
					for (int x = 0; x < Chunk.CHUNK_SIZE; x++) {
						tiles[x][z] = is.readByte();
					}
				}
				newChunk = new Chunk(newChunkX, newChunkZ, newLevel, tiles);
				LoggerUtil.logInfo(getClass(), "Adding old chunk to chunkLoader: (" + newChunk.getChunkX() + ", " + newChunk.getChunkZ() + ")");
				loadedChunks.add(newChunk);
				
			} catch (FileNotFoundException e) {
				if (chunkFile != null) {
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
			
			LoggerUtil.logInfo(getClass(), "Loaded chunk from file.");
			return true;
		}
		else {
			return false;
		}
	}
	
}
