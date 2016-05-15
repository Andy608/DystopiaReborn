package com.bountive.dystopia.world;

import java.util.Random;

import math.Vector3f;

import com.bountive.dystopia.camera.Camera;
import com.bountive.dystopia.camera.FreeRoamCamera;
import com.bountive.dystopia.component.callback.CursorPosCallback;
import com.bountive.dystopia.file.ResourceDirectory;
import com.bountive.dystopia.file.ResourceHelper;
import com.bountive.dystopia.world.component.ChunkSaver;
import com.bountive.dystopia.world.component.WorldChunkManager;

public class World {

	//TODO: EXPAND ON THIS - World.class
	//Add the usual worldy things in this class like the camera and the player and stuff.
	//Maybe add a world generator.
	
	public static final Random rand = new Random();
	
	public static final ResourceDirectory WORLD_DIRECTORY = new ResourceDirectory(ResourceHelper.GAME_APPDATA_DIRECTORY.getFullDirectory(), "worlds", false);
	private ResourceDirectory worldDirectory;
	private ChunkSaver chunkSaver;
	
	private String worldName;
	
	private long seed;
	
	private Camera camera;
	private boolean centerMouse;
	private boolean isPaused;
	
	private WorldChunkManager chunkManager;
	private WorldRenderer renderer;
	
	public World(String name) {
		//Create a new world save.
		worldName = name;
		seed = rand.nextLong();
		
		worldDirectory = new ResourceDirectory(WORLD_DIRECTORY.getFullDirectory(), worldName, false);
		chunkSaver = new ChunkSaver(worldDirectory);
		
		camera = new FreeRoamCamera(new Vector3f(0, 2, 0), new Vector3f(20, 0, 0));
		centerMouse = true;
		isPaused = false;
		chunkManager = new WorldChunkManager(chunkSaver);
		chunkManager.start();
		renderer = new WorldRenderer();
	}
	
	public void update(double deltaTime) {
		if (isPaused) return;
		
		if (centerMouse) {
			CursorPosCallback.centerMouse();
		}
		camera.update((float)deltaTime);
		chunkManager.update((int)camera.getX(), (int)camera.getZ());
	}
	
	public void render(double lerp) {
		renderer.render(this);
	}
	
	public void save() {
		System.out.println("Saving World...");
		chunkSaver.saveChunks(chunkManager.getChunkLoader().loadedChunks);
		System.out.println("Save Complete!");
	}
	
	public ChunkSaver getChunkSaver() {
		return chunkSaver;
	}
	
	public Camera getCamera() {
		return camera;
	}
	
	public String getName() {
		return worldName;
	}
	
	public long getSeed() {
		return seed;
	}
	
	public WorldChunkManager getChunkManager() {
		return chunkManager;
	}
}
