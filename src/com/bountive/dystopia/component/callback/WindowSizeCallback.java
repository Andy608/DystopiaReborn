package com.bountive.dystopia.component.callback;

import org.lwjgl.glfw.GLFWWindowSizeCallback;

import com.bountive.dystopia.camera.CameraMatrixManager;
import com.bountive.dystopia.file.setting.ProgramSettings;

public class WindowSizeCallback extends GLFWWindowSizeCallback {

	@Override
	public void invoke(long window, int width, int height) {
		if (!ProgramSettings.isFullscreenEnabled()) {
			ProgramSettings.updateWindowSize(width, height);
		}
		
		CameraMatrixManager.buildProjectionMatrix();
	}
}
