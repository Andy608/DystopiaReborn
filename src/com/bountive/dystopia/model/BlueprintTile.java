package com.bountive.dystopia.model;

import com.bountive.dystopia.model.util.ModelBuilder;
import com.bountive.dystopia.texture.TilesetList;
import com.bountive.dystopia.tile.Tile;

public class BlueprintTile extends ModelBlueprint {

	public BlueprintTile() {
		initModelBluePrint();
	}
	
	protected void initModelBluePrint() {
		vertexPositions = new float[] {
				-0.5f, 0.0f, 0.5f,
				-0.5f, 0.0f, -0.5f,
				0.5f, 0.0f,  0.5f,
				0.5f, 0.0f, -0.5f};
		
		vertexNormals = new float[] {
				0.0f, 1.0f, 0.0f, 
				0.0f, 1.0f, 0.0f, 
				0.0f, 1.0f, 0.0f, 
				0.0f, 1.0f, 0.0f};
		
//		float uvWidth = 1.0f / (float)TilesetList.TILESET_INDEX_WIDTH;
//		textureCoordinates = new float[] {
//				0.0f, uvWidth,
//				0.0f, 0.0f,
//				uvWidth, uvWidth,
//				uvWidth, 0.0f};
		
		float uvWidth = (1.0f / ((float)TilesetList.TILESET_PIXEL_WIDTH)) * TilesetList.TILE_PIXEL_WIDTH;
		textureCoordinates = new float[] {
				0.0f, uvWidth,
				0.0f, 0.0f,
				uvWidth, uvWidth,
				uvWidth, 0.0f};
		
		indices = new int[] {0, 1, 2, 2, 1, 3};
	}
	
	public ModelTile createModel(Tile tileType) {
		ModelTile t = ModelBuilder.buildTileModel(this, tileType);
		System.out.println(t.getTextureCoordsVBO().getData()[1]);
		return t;
	}
}
