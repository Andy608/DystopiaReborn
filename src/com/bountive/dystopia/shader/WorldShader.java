package com.bountive.dystopia.shader;

import com.bountive.dystopia.camera.Camera;
import com.bountive.dystopia.core.FileResourceTracker;
import com.bountive.dystopia.file.FileResourceLocation;
import com.bountive.dystopia.file.FileResourceLocation.EnumFileExtension;

public class WorldShader extends ShaderBase {

	private int cameraPositionID;
	
	public WorldShader() {
		super(new FileResourceLocation(RESOURCE_DIRECTORY, "worldVertexShader", EnumFileExtension.VS), new FileResourceLocation(RESOURCE_DIRECTORY, "worldFragmentShader", EnumFileExtension.FS));
		FileResourceTracker.addClass(this);
	}
	
	@Override
	public void bindUniformVariables() {
		super.bindUniformVariables();
		cameraPositionID = getUniformLocation("cameraPosition");
	}
	
	public void loadCameraPosition(Camera c) {
		super.loadFloats(cameraPositionID, c.getX(), c.getY(), c.getZ());
	}
}
