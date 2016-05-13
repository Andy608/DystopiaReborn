package com.bountive.dystopia.component.callback;

import java.util.LinkedHashSet;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.opengl.GL11;

import com.bountive.dystopia.file.setting.ControlSettings;
import com.bountive.dystopia.file.setting.ProgramSettings;

public class KeyCallback extends GLFWKeyCallback {

	private static LinkedHashSet<Integer> currentKeys = new LinkedHashSet<>();
	
	@Override
	public void invoke(long windowID, int key, int scancode, int action, int mods) {
		if (action == GLFW.GLFW_RELEASE) {
			currentKeys.remove(key);
			
			if (ControlSettings.moveForwardKey.getCustomKey() == key) {
				ControlSettings.moveForwardKey.setPressed(false);
			}
			else if (ControlSettings.moveBackwardKey.getCustomKey() == key) {
				ControlSettings.moveBackwardKey.setPressed(false);
			}
			else if (ControlSettings.moveLeftKey.getCustomKey() == key) {
				ControlSettings.moveLeftKey.setPressed(false);
			}
			else if (ControlSettings.moveRightKey.getCustomKey() == key) {
				ControlSettings.moveRightKey.setPressed(false);
			}
			else if (ControlSettings.moveUpKey.getCustomKey() == key) {
				ControlSettings.moveUpKey.setPressed(false);
			}
			else if (ControlSettings.moveDownKey.getCustomKey() == key) {
				ControlSettings.moveDownKey.setPressed(false);
			}
		}
		else if (action == GLFW.GLFW_PRESS) {
			currentKeys.add(key);
			
			if (ControlSettings.shutdownKeyBinding.equalsControl(currentKeys)) {
				GLFW.glfwSetWindowShouldClose(windowID, GL11.GL_TRUE);
			}
			else if (ControlSettings.moveForwardKey.getCustomKey() == key) {
				ControlSettings.moveForwardKey.setPressed(true);
			}
			else if (ControlSettings.moveBackwardKey.getCustomKey() == key) {
				ControlSettings.moveBackwardKey.setPressed(true);
			}
			else if (ControlSettings.moveLeftKey.getCustomKey() == key) {
				ControlSettings.moveLeftKey.setPressed(true);
			}
			else if (ControlSettings.moveRightKey.getCustomKey() == key) {
				ControlSettings.moveRightKey.setPressed(true);
			}
			else if (ControlSettings.moveUpKey.getCustomKey() == key) {
				ControlSettings.moveUpKey.setPressed(true);
			}
			else if (ControlSettings.moveDownKey.getCustomKey() == key) {
				ControlSettings.moveDownKey.setPressed(true);
			}
			else if (ControlSettings.fullscreenKey.getCustomKey() == key) {
				currentKeys.clear();
				ProgramSettings.updateFullscreen(!ProgramSettings.isFullscreenEnabled());
			}
		}
	}
}
