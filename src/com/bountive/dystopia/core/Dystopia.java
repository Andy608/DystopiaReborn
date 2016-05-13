package com.bountive.dystopia.core;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import org.lwjgl.Sys;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;

import com.bountive.dystopia.camera.CameraMatrixManager;
import com.bountive.dystopia.component.Window;
import com.bountive.dystopia.component.callback.CursorPosCallback;
import com.bountive.dystopia.debug.logger.LoggerUtil;
import com.bountive.dystopia.file.FileUtil;
import com.bountive.dystopia.model.util.ModelList;
import com.bountive.dystopia.model.util.ModelResourceManager;
import com.bountive.dystopia.texture.SpriteSheetList;
import com.bountive.dystopia.texture.SpriteSheetManager;
import com.bountive.dystopia.tile.TileList;
import com.bountive.dystopia.world.TestRenderer;
import com.bountive.dystopia.world.World;

public class Dystopia {
	
	private static Dystopia instance;
	
	private static final int TICKS_PER_SECOND = 60;
	private static final double TIME_SLICE = 1 / (double)TICKS_PER_SECOND;
	private static final float LAG_CAP = 0.15f;
	private int ticks;
	private int frames;
	
	public static void main(String[] args) {
		String path = (new File(Dystopia.class.getProtectionDomain().getCodeSource().getLocation().getPath())).getParentFile().getPath();
		String decodedPath;
		try {
			decodedPath = URLDecoder.decode(path, "UTF-8");
			System.setProperty("org.lwjgl.librarypath", decodedPath + FileUtil.getFileSeparator(false));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		LoggerUtil.init();
		Thread.setDefaultUncaughtExceptionHandler(LoggerUtil.getInstance());
		instance = new Dystopia();
		instance.run();
	}
	
	private Dystopia() {
		LoggerUtil.logInfo(this.getClass(), "Creating " + Info.NAME + " " + Info.VERSION + " by " + Info.AUTHOR);
	}
	
	
	private void run() {
		LoggerUtil.logInfo(this.getClass(), "Initializing internal structure. Currently running on LWJGL " + Sys.getVersion() + ".");
		
		try {
			FileResourceTracker.init();
			Window.init(Info.NAME + " | " + Info.AUTHOR + " | "+ Info.VERSION);
			Window.buildScreen();
			CameraMatrixManager.init();
//			ModelBuilder.init();//TODO: Might be able to make ModelBuilder class completely static. Must test this.
			GL.createCapabilities();
			ModelResourceManager.init();
			ModelList.initModels();
			SpriteSheetManager.init();
			SpriteSheetList.initSpriteSheets();
			TileList.initTiles();
			loop();
		} catch (Exception e) {
			LoggerUtil.logError(getClass(), e);
		} finally {
			Window.save();
			FileResourceTracker.releaseResource(ModelResourceManager.getInstance());
			FileResourceTracker.releaseProgramResources();
			System.gc();
			System.exit(0);
		}
	}
	
	//TESTING WILL BE REMOVED
//	private Camera camera;
//	private WorldShader shader;
//	private Matrix4f transformationMatrix;
	private TestRenderer testRenderer;
	private World currentWorld;
	/////////////////////////////
	
	private void loop() {
		
		//TESTING WILL BE REMOVED
//		camera = new FreeRoamCamera(new Vector3f(50, 1, 50), new Vector3f(40, 0, 0));
//		shader = new WorldShader();
		testRenderer = new TestRenderer();
		currentWorld = new World("Test World");
//		transformationMatrix = new Matrix4f();
		CursorPosCallback.centerMouse();
		GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
		/////////////////////////////
		
		double lastTime;
		double currentTime;
		double deltaTime;
		double accumulatedTime = 0.0d;
		
		lastTime = GLFW.glfwGetTime();
		while (GLFW.glfwWindowShouldClose(Window.getID()) == GL11.GL_FALSE) {
			currentTime = GLFW.glfwGetTime();
			deltaTime = currentTime - lastTime;
			lastTime = currentTime;
			accumulatedTime += deltaTime;
			
			if (accumulatedTime > LAG_CAP) {
				accumulatedTime = LAG_CAP;
			}
			
			while (accumulatedTime >= TIME_SLICE) {
				accumulatedTime -= TIME_SLICE;
				GLFW.glfwPollEvents();
				update(TIME_SLICE);
			}
			
			render((double)accumulatedTime / TIME_SLICE);
		}
	}
	
	private void update(double deltaTime) {
//		CursorPosCallback.centerMouse();//GLFW.glfwSetInputMode(Window.getID(), GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_NORMAL); for future reference
//		camera.update((float)deltaTime);
		currentWorld.update(deltaTime);
		tick();
	}
	
	private void tick() {
		ticks++;
		if (ticks % 60 == 0) {
			LoggerUtil.logInfo(getClass(), "Ticks: " + ticks + " | Frames: " + frames);
			ticks = 0;
			frames = 0;
		}
	}
	
	private void render(double lerp) {
		frames++;
//		GL11.glClearColor(0.5875f, 0.735f, 1.0f, 1.0f);
		GL11.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		testRenderer.render(currentWorld);
		GLFW.glfwSwapBuffers(Window.getID());
	}
	
	public static Dystopia getMystica() {
		return instance;
	}
}
