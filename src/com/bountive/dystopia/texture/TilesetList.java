package com.bountive.dystopia.texture;

import com.bountive.dystopia.file.FileResourceLocation;
import com.bountive.dystopia.file.FileResourceLocation.EnumFileExtension;
import com.bountive.dystopia.file.ResourceDirectory;

public class TilesetList {

	public static final int TILESET_INDEX_WIDTH = 16;
	public static final int TILE_PIXEL_WIDTH = 32;
	public static final int TILESET_PIXEL_WIDTH = 544;
	private static final ResourceDirectory TILESET_DIRECTORY = new ResourceDirectory(TextureLoader.IMAGES_DIRECTORY.getFullDirectory(), "tileset", true);
	
	public static Texture tilesetSpriteSheet;
	
	public static void init() {
		tilesetSpriteSheet = TextureLoader.loadTexture(new FileResourceLocation(TILESET_DIRECTORY, "terrain", EnumFileExtension.PNG));
	}
}
