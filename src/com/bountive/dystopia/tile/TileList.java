package com.bountive.dystopia.tile;

public class TileList {

	private static TileRegistry tileRegistry;
	private static int tileCounter;
	public static Tile grassTile;
	public static Tile waterTile;
	
	public static void initTiles() {
		grassTile = new Tile(0, "grass_tile", 0, 0); tileCounter++;
		waterTile = new Tile(1, "water_tile", 0, 1); tileCounter++;
		
		tileRegistry = new TileRegistry(tileCounter);
		registerTiles();
	}
	
	private static void registerTiles() {
		tileRegistry.registerTile(grassTile);
		tileRegistry.registerTile(waterTile);
	}
	
	public static TileRegistry getTileRegistry() {
		return tileRegistry;
	}
}
