package com.bountive.dystopia.world.component;

import java.util.ArrayList;
import java.util.List;

public class WorldChunkManager {

	/**
	 * This class keeps track of all the chunks in the world. First, it checks to see if chunks can be loaded up
	 * from a file. If it does not find anything it creates a new chunk.
	 */
	private ArrayList<Chunk> activeChunks;
	private ChunkLoader chunkLoader;
	
	public WorldChunkManager() {
		activeChunks = new ArrayList<>(64);
		chunkLoader = new ChunkLoader();
	}
	
	public void update(ChunkSaver chunkSaver, int playerX, int playerZ) {
		//Eventually we will update chunks on player action and only load new chunks when the player moves. (Maybe)
		EnumQuadrant quadrant = EnumQuadrant.getQuadrant(playerX, playerZ);
		
		int chunkX, chunkZ;
		
		if (quadrant == EnumQuadrant.QUADRANT_1) {
			chunkX = playerX / Chunk.CHUNK_SIZE;
			chunkZ = playerZ / Chunk.CHUNK_SIZE;
		}
		else if (quadrant == EnumQuadrant.QUADRANT_2) {
			chunkX = (playerX - Chunk.CHUNK_SIZE) / Chunk.CHUNK_SIZE;
			chunkZ = playerZ / Chunk.CHUNK_SIZE;
		}
		else if (quadrant == EnumQuadrant.QUADRANT_3) {
			chunkX = (playerX - Chunk.CHUNK_SIZE) / Chunk.CHUNK_SIZE;
			chunkZ = (playerZ -Chunk.CHUNK_SIZE) / Chunk.CHUNK_SIZE;
		}
		else {
			chunkX = playerX / Chunk.CHUNK_SIZE;
			chunkZ = (playerZ - Chunk.CHUNK_SIZE) / Chunk.CHUNK_SIZE;
		}
		
//		System.out.println(chunkX + " | " + chunkZ);
		
		loadChunks(chunkSaver, quadrant, chunkX, chunkZ);
		unloadChunks(chunkSaver, quadrant, chunkX, chunkZ);
	}
	
	public void loadChunks(ChunkSaver chunkSaver, EnumQuadrant quadrant, int chunkX, int chunkZ) {
		//Step 1. Load needed chunks/create new ones
		//Checks if the current chunk and surrounding chunks are in the save file.
		//Loads all the chunks it finds into queue list and creates the chunks it can't find.
		chunkLoader.loadRequiredChunks(chunkSaver, quadrant, chunkX, chunkZ);
		
		//Step 2. Add the loaded/created chunks into the world.
		addChunksToWorld(chunkLoader.loadedChunks);
	}
	
	private void unloadChunks(ChunkSaver chunkSaver, EnumQuadrant quadrant, int chunkX, int chunkZ) {
		//Step 3. Unload unneeded chunks from the loader.
		//Removes unneeded chunks from the chunk loader queue based on player distance.
		List<Chunk> oldChunks = chunkLoader.cleanQueue(chunkSaver, quadrant, chunkX, chunkZ);
		
//		System.out.println(oldChunks.size() + " OLD CHUNKS");
		
		//Step 4. Remove unneeded chunks from the world.
		removeChunksFromWorld(oldChunks);
	}
	
	private void addChunksToWorld(List<Chunk> chunks) {
		boolean addChunk = true;
		for (Chunk loadedChunk : chunks) {
			for (int i = 0; i < activeChunks.size(); i++) {
				if (loadedChunk.equals(activeChunks.get(i))) {
					addChunk = false;
					continue;
				}
			}
			
			if (addChunk) {
				System.out.println("Adding chunk to world: (" + loadedChunk.getChunkX() + ", " + loadedChunk.getChunkZ() + ")");
				activeChunks.add(loadedChunk);
				System.out.println("Active chunks in world: " + activeChunks.size());
			}
			addChunk = true;
		}
		
//		System.out.println(activeChunks.size());
	}
	
	private void removeChunksFromWorld(List<Chunk> chunks) {
		for (Chunk c : chunks) {
			activeChunks.remove(c);
			//Update the world with the new active chunks list.
		}
//		System.out.println(activeChunks.size());
	}
	
	public ArrayList<Chunk> getActiveChunks() {
		return activeChunks;
	}
	
	public ChunkLoader getChunkLoader() {
		return chunkLoader;
	}
}
