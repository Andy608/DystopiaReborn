package com.bountive.dystopia.world.generation;

import java.util.ArrayList;
import java.util.List;

import com.bountive.dystopia.debug.logger.LoggerUtil;
import com.bountive.dystopia.file.ResourceDirectory;

public class WorldChunkManager extends Thread {

	/**
	 * This class keeps track of all the chunks in the world. First, it checks to see if chunks can be loaded up
	 * from a file. If it does not find anything it creates a new chunk.
	 */
	private volatile ArrayList<Chunk> activeChunks;
	
	public final ResourceDirectory LEVEL;
	public final int CHUNK_BYTE_LENGTH;
	public final int REGION_SIZE;
	
	private volatile ChunkLoader chunkLoader;
	private volatile ChunkSaver chunkSaver;
	private volatile int playerX, playerZ;
	
	private volatile boolean finished = false, isUpdate = false;
	
	public WorldChunkManager(ResourceDirectory worldDirectory) {
		super("Chunk Manager");
		activeChunks = new ArrayList<>(64);
		
		LEVEL = new ResourceDirectory(worldDirectory.getFullDirectory(), "level", false);
		CHUNK_BYTE_LENGTH = (2 * Integer.BYTES) + 1 + Chunk.CHUNK_SIZE * Chunk.CHUNK_SIZE;
		REGION_SIZE = 16;
		
		chunkLoader = new ChunkLoader();
		chunkSaver = new ChunkSaver();
	}
	
	@Override
	public void run() {
		while (!finished) {
			if (!isUpdate) continue;
			
			//Eventually we will update chunks on player action and only load new chunks when the player moves. (Maybe)
			EnumQuadrant quadrant = EnumQuadrant.getQuadrant(playerX, playerZ);
			int chunkX = EnumQuadrant.convertXBasedOnQuadrant(quadrant, playerX, Chunk.CHUNK_SIZE);
			int chunkZ = EnumQuadrant.convertZBasedOnQuadrant(quadrant, playerZ, Chunk.CHUNK_SIZE);
			
//			System.out.println(chunkX + " | " + chunkZ);
			
			loadChunks(quadrant, chunkX, chunkZ);
			unloadChunks(chunkSaver, quadrant, chunkX, chunkZ);
			isUpdate = false;
		}
		LoggerUtil.logInfo(getClass(), "WorldChunkManager stopped.");
	}
	
	public void stopRunning() {
		finished = true;
	}
	
	public void update(int newPlayerX, int newPlayerZ) {
		isUpdate = true;
		playerX = newPlayerX;
		playerZ = newPlayerZ;
	}
	
	public void loadChunks(EnumQuadrant quadrant, int chunkX, int chunkZ) {
		//Step 1. Load needed chunks/create new ones
		//Checks if the current chunk and surrounding chunks are in the save file.
		//Loads all the chunks it finds into queue list and creates the chunks it can't find.
		chunkLoader.loadRequiredChunks(this, quadrant, chunkX, chunkZ);
		
		//Step 2. Add the loaded/created chunks into the world.
		addChunksToWorld(chunkLoader.loadedChunks);
	}
	
	private void unloadChunks(ChunkSaver chunkSaver, EnumQuadrant quadrant, int chunkX, int chunkZ) {
		//Step 3. Unload unneeded chunks from the loader.
		//Removes unneeded chunks from the chunk loader queue based on player distance.
		List<Chunk> oldChunks = chunkLoader.cleanQueue(this, chunkSaver, quadrant, chunkX, chunkZ);
		
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
//				System.out.println("Adding chunk to world: (" + loadedChunk.getChunkX() + ", " + loadedChunk.getChunkZ() + ")");
				activeChunks.add(loadedChunk);
//				System.out.println("Active chunks in world: " + activeChunks.size());
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
	
	public ChunkSaver getChunkSaver() {
		return chunkSaver;
	}
}
