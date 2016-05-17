package com.bountive.dystopia.texture;

import com.bountive.dystopia.core.IRelease;
import com.bountive.dystopia.file.FileResourceLocation;
import com.bountive.dystopia.file.FileResourceLocation.EnumFileExtension;
import com.bountive.dystopia.file.ResourceDirectory;

public class SpriteSheet implements IRelease {

	//TODO: EXPAND ON THIS - SpriteSheet.class
	
	//Creates a bytebuffer from the image. Get the width and height of the image and make sure it is divisible by the width and height. If not then just cut the image to be smaller to fit.
	//Idk if needed, but dispose of the image in the release() section.
	
	private FileResourceLocation spriteSheetLocation;//use to create buffered image / byte buffer.
	private String spriteSheetName;
	private boolean isActive;
	public static final int TILE_WIDTH = 16;
	
	public SpriteSheet(ResourceDirectory dir, String name) {
		spriteSheetLocation = new FileResourceLocation(dir, name, EnumFileExtension.PNG);
		spriteSheetName = name;
	}
	
	public String getName() {
		return spriteSheetName;
	}
	
	public void activate() {
		isActive = true;
		//Add spritesheet into memory.
	}
	
	public boolean isActive() {
		return isActive;
	}
	
	@Override
	public void release() {
		isActive = false;
		//Remove spritesheet from memory.
	}
}
