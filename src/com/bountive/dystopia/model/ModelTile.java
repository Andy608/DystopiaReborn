package com.bountive.dystopia.model;

import com.bountive.dystopia.model.util.ModelRaw;
import com.bountive.dystopia.model.util.VBOWrapper;
import com.bountive.dystopia.tile.Tile;

public class ModelTile extends ModelRaw {

	private Tile tile;
	private VBOWrapper textureCoords;
	
	public ModelTile(Tile t, int arrayID, int[] ind, VBOWrapper positionsVBO, VBOWrapper colorsVBO, VBOWrapper normalsVBO, VBOWrapper textureCoordsVBO) {
		super(arrayID, ind, new VBOWrapper[] {positionsVBO, colorsVBO, normalsVBO, textureCoordsVBO});
		tile = t;
		textureCoords = textureCoordsVBO;
	}
	
	public VBOWrapper getTextureCoordsVBO() {
		return textureCoords;
	}
	
	public Tile getTile() {
		return tile;
	}
}
