package com.bountive.dystopia.model;

public class ModelTile extends ModelBluePrint {

	public ModelTile() {
		super(new float[] {
				-0.5f, 0.0f, 0.5f,
				-0.5f, 0.0f, -0.5f,
				0.5f, 0.0f,  0.5f,
				0.5f, 0.0f, -0.5f, 
		},
		new int[]{0, 1, 2, 2, 1, 3},
		new float[] {},
		new float[] {});
	}
}
