package com.bountive.dystopia.world.component;

import java.util.ArrayList;

import com.bountive.dystopia.debug.logger.LoggerUtil;
import com.bountive.dystopia.file.setting.ProgramSettings;
import com.bountive.dystopia.world.component.Chunk.EnumQuadrant;

public class ChunkLoader {

	protected ArrayList<Chunk> loadedChunks;
	ArrayList<Chunk> removedChunks;
	
	public ChunkLoader() {
		loadedChunks = new ArrayList<>(64);
		removedChunks = new ArrayList<>();
	}
	
	public ArrayList<Chunk> cleanQueue(EnumQuadrant quadrant, int chunkX, int chunkZ) {
		//Step1. Get the render distance.
		int renderDistance = ProgramSettings.getCustomRenderDistance();
		//Step2. For all the chunks in loadedChunks, check if they are in range from the current chunk passed in.
		removedChunks.clear();
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
		//Step5. Return removedChunks.
		return removedChunks;
	}
	
	//TODO: Friday: Implement this method!!
	public boolean loadRequiredChunks(EnumQuadrant quadrant, int chunkX, int chunkZ) {
		//Step1. Get the render distance.
		int renderDistance = ProgramSettings.getCustomRenderDistance();
		//Step2. Check if the current chunk and surrounding chunks are in loadedChunks.
		
			//
		
		for (Chunk c : loadedChunks) {
			if (c.isChunk(quadrant, chunkX, chunkZ)) {
				
			}
		}
		
		//Step3. If all chunks are in loadedChunks return false.
		//Step4. For the ones that aren't in loadedChunks:
		// Check save file first.
		// If not in save file create new chunk.
	
		//Step 5. If in save file, create new chunk with info from save file and add it to loadedChunks.
			//If not in save file, create new chunk with new info and add it to loadedChunks.
		//Step 6. Return true.
	}
	
	private boolean getSurroundingChunks(EnumQuadrant quadrant, int chunkX, int chunkZ) {
		
	}
}
