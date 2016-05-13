package com.bountive.dystopia.world.component;

import java.util.ArrayList;
import java.util.List;

import com.bountive.dystopia.world.component.Chunk.EnumQuadrant;

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
	
	public void update(int playerX, int playerZ) {
		//Eventually we will update chunks on player action and only load new chunks when the player moves. (Maybe)
		EnumQuadrant quadrant;
		if (playerX >= 0 && playerZ >= 0) {
			quadrant = EnumQuadrant.QUADRANT_1;
		}
		else if (playerX < 0 && playerZ >= 0) {
			quadrant = EnumQuadrant.QUADRANT_2;
		}
		else if (playerX < 0 && playerZ < 0) {
			quadrant = EnumQuadrant.QUADRANT_3;
		}
		else {
			quadrant = EnumQuadrant.QUADRANT_4;
		}
		
		int chunkX = playerX / Chunk.CHUNK_SIZE;
		int chunkZ = playerZ / Chunk.CHUNK_SIZE;
		
		loadChunks(quadrant, chunkX, chunkZ);
		unloadChunks(quadrant, chunkX, chunkZ);
	}
	
	public void loadChunks(EnumQuadrant quadrant, int chunkX, int chunkZ) {
		//Step 1. Load needed chunks/create new ones
		//Checks if the current chunk and surrounding chunks are in the save file.
		//Loads all the chunks it finds into queue list and creates the chunks it can't find.
		if (!chunkLoader.loadRequiredChunks(quadrant, chunkX, chunkZ)) {
			return;
		}
		
		//Step 2. Add the loaded/created chunks into the world.
		addChunksToWorld(chunkLoader.loadedChunks);
	}
	
	private void unloadChunks(EnumQuadrant quadrant, int chunkX, int chunkZ) {
		//Step 3. Unload unneeded chunks from the loader.
		//Removes unneeded chunks from the chunk loader queue based on player distance.
		List<Chunk> oldChunks = chunkLoader.cleanQueue(quadrant, chunkX, chunkZ);
		
		//Step 4. Remove unneeded chunks from the world.
		removeChunksFromWorld(oldChunks);
	}
	
	private void addChunksToWorld(List<Chunk> chunks) {
		for (Chunk c : chunks) {
			activeChunks.add(c);
			//Update the world with the new active chunks list.
		}
	}
	
	private void removeChunksFromWorld(List<Chunk> chunks) {
		for (Chunk c : chunks) {
			activeChunks.remove(c);
			//Update the world with the new active chunks list.
		}
	}
}
