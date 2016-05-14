package com.bountive.dystopia.world;

import java.util.Random;

import math.Vector3f;

import com.bountive.dystopia.camera.Camera;
import com.bountive.dystopia.camera.FreeRoamCamera;
import com.bountive.dystopia.component.callback.CursorPosCallback;
import com.bountive.dystopia.world.component.WorldChunkManager;
import com.bountive.dystopia.world.component.WorldSaveHandler;

public class World {

	//TODO: EXPAND ON THIS - World.class
	//Create 'chunks' or zones to handle different tiles.
	//Design a saving and loading system for worlds.
	//Create a new custom renderer that takes in a world and draws tiles based on the saved world and draws different images depending on the tile and biome.
	
	//Add the usual worldy things in this class like the camera and the player and stuff.
	//Maybe add a world generator.
	
	public static final Random rand = new Random();
	
	private String worldName;
	
	private long seed;
	
	private Camera camera;
	private boolean centerMouse;
	private boolean isPaused;
	
	private WorldSaveHandler saveHandler;
	private WorldChunkManager chunkManager;
	private WorldRenderer renderer;
	
	public World(String name) {
		//Create a new world save.
		worldName = name;
		seed = rand.nextLong();
		camera = new FreeRoamCamera(new Vector3f(2, 0, 0), new Vector3f(20, 0, 0));
		centerMouse = true;
		isPaused = false;
		chunkManager = new WorldChunkManager();
		saveHandler = new WorldSaveHandler(worldName);
		renderer = new WorldRenderer();
	}
	
	public void update(double deltaTime) {
		if (isPaused) return;
		
		if (centerMouse) {
			CursorPosCallback.centerMouse();
		}
		camera.update((float)deltaTime);
		chunkManager.update(saveHandler.getChunkSaver(), (int)camera.getX(), (int)camera.getZ());
	}
	
	public void render(double lerp) {
		renderer.render(this);
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
	
	public WorldSaveHandler getSaveHandler() {
		return saveHandler;
	}
}
