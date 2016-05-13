package com.bountive.dystopia.camera;

import math.Matrix4f;
import math.Vector2f;

import com.bountive.dystopia.debug.logger.LoggerUtil;
import com.bountive.dystopia.file.setting.ProgramSettings;

public class CameraMatrixManager {

	private static CameraMatrixManager instance;
	private Matrix4f projectionMatrix;
	
	//TEMP
	private static final float NEAR_PLANE = 0.01f;
	private static final float FAR_PLANE = 200.0f;
	//////
	
	private CameraMatrixManager() {
		projectionMatrix = new Matrix4f();
	}
	
	public static void init() {
		if (instance == null) {
			instance = new CameraMatrixManager();
			instance.buildMatrices();
		}
		else {
			LoggerUtil.logWarn(CameraMatrixManager.class, instance.getClass().getSimpleName() + ".class is already initialized.");
		}
	}
	
	private void buildMatrices() {
		buildProjectionMatrix();
	}
	
	public static void buildProjectionMatrix() {
		if (instance == null) init();
		
		Vector2f windowSize = new Vector2f(ProgramSettings.getCurrentWindowWidth(), ProgramSettings.getCurrentWindowHeight());
		float aspectRatio = (float)windowSize.x / (float)windowSize.y;
		float FOV = ProgramSettings.getCustomFOV();
		
		instance.projectionMatrix.setIdentity();
		instance.projectionMatrix.m00 = 1f / (float)(Math.tan(Math.toRadians(FOV) / 2f));
		instance.projectionMatrix.m11 = aspectRatio / (float)(Math.tan(Math.toRadians(FOV) / 2f));
		instance.projectionMatrix.m22 = (NEAR_PLANE + FAR_PLANE) / (NEAR_PLANE - FAR_PLANE);
		instance.projectionMatrix.m23 = -1f;
		instance.projectionMatrix.m32 = (2 * NEAR_PLANE * FAR_PLANE) / (NEAR_PLANE - FAR_PLANE);
		instance.projectionMatrix.m33 = 0f;
	}
	
	public static Matrix4f getProjectionMatrix() {
		return instance.projectionMatrix;
	}
}
