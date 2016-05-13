package com.bountive.dystopia.component.callback;

import org.lwjgl.glfw.Callbacks;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWFramebufferSizeCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWWindowFocusCallback;
import org.lwjgl.glfw.GLFWWindowPosCallback;
import org.lwjgl.glfw.GLFWWindowSizeCallback;

public class WindowCallbackManager {

	private GLFWErrorCallback errorCallback;
	private GLFWWindowFocusCallback windowFocusCallback;
	private GLFWWindowSizeCallback windowSizeCallback;
	private GLFWFramebufferSizeCallback framebufferSizeCallback;
	private GLFWWindowPosCallback windowPosCallback;
	private GLFWKeyCallback keyCallback;
	private GLFWCursorPosCallback cursorPosCallback;
	
	public WindowCallbackManager(long windowID) {
		
		GLFW.glfwSetErrorCallback(errorCallback = Callbacks.errorCallbackPrint(System.err));
		GLFW.glfwSetWindowFocusCallback(windowID, windowFocusCallback = new WindowFocusCallback());
		GLFW.glfwSetWindowSizeCallback(windowID, windowSizeCallback = new WindowSizeCallback());
		GLFW.glfwSetFramebufferSizeCallback(windowID, framebufferSizeCallback = new FramebufferCallback());
		GLFW.glfwSetWindowPosCallback(windowID, windowPosCallback = new WindowPosCallback());
		GLFW.glfwSetKeyCallback(windowID, keyCallback = new KeyCallback());
		GLFW.glfwSetCursorPosCallback(windowID, cursorPosCallback = new CursorPosCallback());
	}
	
	public void release() {
		errorCallback.release();
		windowFocusCallback.release();
		windowSizeCallback.release();
		framebufferSizeCallback.release();
		windowPosCallback.release();
		keyCallback.release();
		cursorPosCallback.release();
	}
}
