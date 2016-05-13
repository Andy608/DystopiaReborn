package com.bountive.dystopia.tile;

import com.bountive.dystopia.texture.SpriteSheet;
import com.bountive.dystopia.texture.SpriteSheetList;

public class Tile {

	private int tileID;
	private String unlocalizedName;
	private MovementAnimType movementType;
	private SpriteSheet spriteSheet;
	private int spriteSheetIndexX;
	private int spriteSheetIndexY;
	
	public Tile(int id, String name, int spriteIndexX, int spriteIndexY) {
		this(id, name, MovementAnimType.WALK, SpriteSheetList.tileTerrainSpriteSheet, spriteIndexX, spriteIndexY);
	}
	
	public Tile(int id, String name, MovementAnimType moveType, SpriteSheet sheet, int spriteIndexX, int spriteIndexY) {
		tileID = id;
		unlocalizedName = name;
		movementType = moveType;
		spriteSheet = sheet;
		spriteSheetIndexX = spriteIndexX;
		spriteSheetIndexY = spriteIndexY;
	}
	
	public int getID() {
		return tileID;
	}
	
	public String getUnlocalizedName() {
		return unlocalizedName;
	}
	
	public MovementAnimType getMovementType() {
		return movementType;
	}
	
	public SpriteSheet getSpriteSheet() {
		return spriteSheet;
	}
	
	public int getSpriteIndexX() {
		return spriteSheetIndexX;
	}
	
	public int getSpriteIndexY() {
		return spriteSheetIndexY;
	}
	
	public enum MovementAnimType {
		WALK,
		SWIM,
		NO_ENTRY;
	}
}
