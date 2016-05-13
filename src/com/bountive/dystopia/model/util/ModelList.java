package com.bountive.dystopia.model.util;

import com.bountive.dystopia.model.ModelTile;

public class ModelList {

	public static ModelRaw modelTile;
	
	//IDK IF I WANNA DO IT LIKE THIS BUT WE'LL SEE IN THE FUTURE
	public static void initModels() {
		modelTile = ModelBuilder.buildModel(new ModelTile());
	}
}
