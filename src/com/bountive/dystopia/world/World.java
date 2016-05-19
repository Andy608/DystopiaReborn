package com.bountive.dystopia.world;

import java.util.Random;

import math.Vector3f;

import com.bountive.dystopia.camera.Camera;
import com.bountive.dystopia.camera.FreeRoamCamera;
import com.bountive.dystopia.component.callback.CursorPosCallback;
import com.bountive.dystopia.file.ResourceDirectory;
import com.bountive.dystopia.file.ResourceHelper;
import com.bountive.dystopia.world.generation.WorldChunkManager;

public class World {

	//TODO: EXPAND ON THIS - World.class
	//Add the usual worldy things in this class like the camera and the player and stuff.
	//Maybe add a world generator.
	
	public static final Random rand = new Random();
	
	public static final ResourceDirectory WORLD_DIRECTORY = new ResourceDirectory(ResourceHelper.GAME_APPDATA_DIRECTORY.getFullDirectory(), "worlds", false);
	private ResourceDirectory worldDirectory;
	
	private String worldName;
	
	private long seed;
	
	private Camera camera;
	private boolean centerMouse;
	private boolean isPaused;
	
	private WorldChunkManager chunkManager;
	private WorldRenderer worldRenderer;
	
	public World(String name) {
		//Create a new world save.
		worldName = name;
		seed = rand.nextLong();
		
		worldDirectory = new ResourceDirectory(WORLD_DIRECTORY.getFullDirectory(), worldName, false);
//		camera = new BirdsEyeCamera(new Vector3f(0, 1, 0), new Vector3f(70, 0, 0));
		camera = new FreeRoamCamera(new Vector3f(0, 1, 0), new Vector3f(70, 0, 0));
		centerMouse = true;
		isPaused = false;
		chunkManager = new WorldChunkManager(worldDirectory);
		chunkManager.start();
		worldRenderer = new WorldRenderer();
	}
	
	public void update(double deltaTime) {
		if (isPaused) return;
		
		if (centerMouse) {
			CursorPosCallback.centerMouse();
		}
		camera.update((float)deltaTime);
//		ModelResourceManager.update();
		chunkManager.update((int)camera.getX(), (int)camera.getZ());
	}
	
	public void render(double lerp) {
		worldRenderer.render(this);
	}
	
	public void save() {
		System.out.println("Saving World...");
		chunkManager.stopRunning();
		chunkManager.getChunkSaver().saveChunks(chunkManager, chunkManager.getChunkLoader().loadedChunks);
		System.out.println("Save Complete!");
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
