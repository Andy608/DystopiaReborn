package com.bountive.dystopia.texture;

import com.bountive.dystopia.file.ResourceDirectory;

public class SpriteSheetList {

	private static final ResourceDirectory IMAGES_DIRECTORY = new ResourceDirectory("res/graphics", "images", true);
	public static SpriteSheet tileTerrainSpriteSheet;
	
	public static void initSpriteSheets() {
		tileTerrainSpriteSheet = new SpriteSheet(IMAGES_DIRECTORY, "terrain");
		
		registerSpriteSheets();
	}
	
	private static void registerSpriteSheets() {
		SpriteSheetManager.addSpriteSheet(tileTerrainSpriteSheet);
	}
}
