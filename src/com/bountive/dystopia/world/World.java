package com.bountive.dystopia.world;

import math.Vector3f;

import com.bountive.dystopia.camera.Camera;
import com.bountive.dystopia.camera.FreeRoamCamera;
import com.bountive.dystopia.component.callback.CursorPosCallback;
import com.bountive.dystopia.world.component.WorldChunkManager;

public class World {

	//TODO: EXPAND ON THIS - World.class
	//Create 'chunks' or zones to handle different tiles.
	//Design a saving and loading system for worlds.
	//Create a new custom renderer that takes in a world and draws tiles based on the saved world and draws different images depending on the tile and biome.
	
	//Add the usual worldy things in this class like the camera and the player and stuff.
	//Maybe add a world generator.
	
	private String worldName;
	
	private Camera camera;
	private boolean centerMouse;
	private boolean isPaused;
	
	private WorldChunkManager chunkManager;
	
	public World(String name) {
		//Create a new world save.
		worldName = name;
		camera = new FreeRoamCamera(new Vector3f(50, 1, 50), new Vector3f(40, 0, 0));
		centerMouse = true;
		isPaused = false;
		chunkManager = new WorldChunkManager();
	}
	
	public void update(double deltaTime) {
		if (isPaused) return;
		
		if (centerMouse) {
			CursorPosCallback.centerMouse();
		}
		camera.update((float)deltaTime);
		chunkManager.update((int)camera.getX(), (int)camera.getZ());
	}
	
	public Camera getCamera() {
		return camera;
	}
	
	public String getName() {
		return worldName;
	}
}
