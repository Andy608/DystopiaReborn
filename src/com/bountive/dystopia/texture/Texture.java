package com.bountive.dystopia.texture;

import org.lwjgl.opengl.GL11;

import com.bountive.dystopia.core.IRelease;
import com.bountive.dystopia.file.FileResourceLocation;

public class Texture implements IRelease {

	private int textureID;
	private FileResourceLocation textureLocation;
	
	public Texture(int id, FileResourceLocation location) {
		textureID = id;
		textureLocation = location;
	}
	
	public String getFileName() {
		return textureLocation.getFileName();
	}
	
	public FileResourceLocation getFilePath() {
		return textureLocation;
	}
	
	public int getID() {
		return textureID;
	}
	
	@Override
	public void release() {
		GL11.glDeleteTextures(textureID);
	}
}
