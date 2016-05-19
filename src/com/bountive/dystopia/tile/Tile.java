package com.bountive.dystopia.tile;

import com.bountive.dystopia.model.BlueprintTile;
import com.bountive.dystopia.model.ModelTile;
import com.bountive.dystopia.texture.Texture;
import com.bountive.dystopia.texture.TilesetList;

public class Tile {

	public static final BlueprintTile TILE_BLUEPRINT = new BlueprintTile();
	
	private int tileID;
	private String unlocalizedName;
	private MovementAnimType movementType;
	private ModelTile tileModel;
	private Texture spriteSheet;
	private int spriteSheetIndexX;
	private int spriteSheetIndexY;
	
	public Tile(int id, String name, int spriteIndexX, int spriteIndexY) {
		this(id, name, MovementAnimType.WALK, TilesetList.tilesetSpriteSheet, spriteIndexX, spriteIndexY);
	}
	
	public Tile(int id, String name, MovementAnimType moveType, Texture sheet, int spriteIndexX, int spriteIndexY) {
		tileID = id;
		unlocalizedName = name;
		movementType = moveType;
		spriteSheet = sheet;
		spriteSheetIndexX = spriteIndexX;
		spriteSheetIndexY = spriteIndexY;
	}
	
	public void buildModel() {
		tileModel = TILE_BLUEPRINT.createModel(this);
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
	
	public ModelTile getModel() {
		return tileModel;
	}
	
	public Texture getSpriteSheet() {
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
