package com.bountive.dystopia.component.callback;

import org.lwjgl.glfw.GLFWWindowPosCallback;

import com.bountive.dystopia.file.setting.ProgramSettings;

public class WindowPosCallback extends GLFWWindowPosCallback {

	@Override
	public void invoke(long window, int xpos, int ypos) {
		if (!ProgramSettings.isFullscreenEnabled()) {
			ProgramSettings.updateWindowPosition(xpos, ypos);
		}
	}
}
