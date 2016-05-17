package com.bountive.dystopia.tile;

public class TileList {

	private static TileRegistry tileRegistry;
	private static int tileCounter;
	public static Tile testTile;
	
	public static void initTiles() {
		testTile = new Tile(0, "test_tile", 0, 0); tileCounter++;
		tileRegistry = new TileRegistry(tileCounter);
		registerTiles();
	}
	
	private static void registerTiles() {
		tileRegistry.registerTile(testTile);
	}
	
	public static TileRegistry getTileRegistry() {
		return tileRegistry;
	}
}
