package com.bountive.dystopia.tile;

public class TileRegistry {

	private Tile[] tileList;
	
	public TileRegistry(int tileListCapacity) {
		tileList = new Tile[tileListCapacity];
	}
	
	public void registerTile(Tile t) {
		t.buildModel();
		tileList[t.getID()] = t;
	}
	
	public Tile getTileByID(int tileID) {
		return tileList[tileID];
	}
}
