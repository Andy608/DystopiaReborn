package com.bountive.dystopia.world.component;

import com.bountive.dystopia.file.ResourceDirectory;
import com.bountive.dystopia.file.ResourceHelper;
import com.bountive.dystopia.world.World;

public class WorldSaveHandler {

	public static final ResourceDirectory WORLD_DIRECTORY = new ResourceDirectory(ResourceHelper.GAME_APPDATA_DIRECTORY.getFullDirectory(), "worlds", false);
	//Auto save option
	private ResourceDirectory worldDirectory;
	private ChunkSaver chunkSaver;
	private String saveName;
	
	public WorldSaveHandler(String worldName) {
		saveName = worldName;
		worldDirectory = new ResourceDirectory(WORLD_DIRECTORY.getFullDirectory(), saveName, false);
		chunkSaver = new ChunkSaver(worldDirectory);
	}
	
	public void save(World world) {
		System.out.println("Saving World...");
		chunkSaver.saveChunks(world.getChunkManager().getChunkLoader().loadedChunks);
		System.out.println("Save Complete!");
	}
	
	public ChunkSaver getChunkSaver() {
		return chunkSaver;
	}
}
