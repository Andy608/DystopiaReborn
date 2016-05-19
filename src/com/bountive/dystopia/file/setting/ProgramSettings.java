package com.bountive.dystopia.file.setting;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

import math.Vector2f;

import com.bountive.dystopia.camera.CameraMatrixManager;
import com.bountive.dystopia.component.Window;
import com.bountive.dystopia.debug.logger.LoggerUtil;
import com.bountive.dystopia.file.FileResourceLocation;
import com.bountive.dystopia.file.FileResourceLocation.EnumFileExtension;
import com.bountive.dystopia.model.util.ModelResourceManager;

public class ProgramSettings extends SettingsBase {

	private static final FileResourceLocation PROGRAM_SETTINGS = new FileResourceLocation(SETTINGS_DIR, "program_settings", EnumFileExtension.PROPERTIES);
	private static ProgramSettings instance;

	private Vector2fSetting windowSize;
	private Vector2fSetting windowPosition;
	
	private BooleanSetting saveWindowSize;
	private BooleanSetting saveWindowPosition;
	
	private BooleanSetting fullscreen;
	
	private BooleanSetting vSync;
	private ClampedIntegerSetting fieldOfView;
	private ClampedIntegerSetting renderDistance;
	
	private ProgramSettings() {
		initDefaultSettings();
	}
	
	public static void init() throws IllegalStateException {
		if (instance == null) {
			instance = new ProgramSettings();
		}
		else {
			LoggerUtil.logWarn(instance.getClass(), instance.getClass().getSimpleName() + ".class has already been initialized.");
		}
	}
	
	@Override
	protected void initDefaultSettings() {
		windowSize = new Vector2fSetting("window_size", new Vector2f(Window.getPrimaryMonitorWidth() / 2, Window.getPrimaryMonitorHeight() / 2));
		saveWindowSize = new BooleanSetting("save_window_size", true);
		windowPosition = new Vector2fSetting("window_position", new Vector2f((Window.getPrimaryMonitorWidth() - windowSize.getDefaultVector2f().x) / 2, (Window.getPrimaryMonitorHeight() - windowSize.getDefaultVector2f().y) / 2));
		saveWindowPosition = new BooleanSetting("save_window_position", true);
		fullscreen = new BooleanSetting("fullscreen", false);
		vSync = new BooleanSetting("vSync", true);
		fieldOfView = new ClampedIntegerSetting("fov", 70, 30, 110);
		renderDistance = new ClampedIntegerSetting("render_distance", 2, 0, 4);
	}

	@Override
	public void loadSettingsFromFile() {
		File location = new File(PROGRAM_SETTINGS.getFullPath());
		setDefaultSettings();
		
		if (location.exists()) {
			try {
				String[] settings = readOptionsFile(location);
				
				for (String s : settings) {
					String settingAttrib = s.substring(0, s.indexOf(DEFAULT_DELIMITER));
					
					if (settingAttrib.equals(windowSize.getSettingName())) {
						try {
							int width = getSingleIntegerFromSetting(s, (int)windowSize.getDefaultVector2f().x, DEFAULT_DELIMITER, SEPARATOR);
							int height = getSingleIntegerFromSetting(s, (int)windowSize.getDefaultVector2f().y, SEPARATOR);
							
							if (width >= Window.getPrimaryMonitorWidth() || height >= Window.getPrimaryMonitorHeight()) {
								windowSize.resetVector2f();
							}
							else {
								windowSize.setCustomVector2f(getVector2fValue(new Vector2f(width, height), windowSize.getDefaultVector2f()));
							}
						} catch (NumberFormatException e) {
							LoggerUtil.logWarn(getClass(), e, PROGRAM_SETTINGS.getFileNameWithExtension() + " is corrupt! Did you edit this file? Unable to get correct windowSize. Using default value instead.", true);
							windowSize.resetVector2f();
						}
						continue;
					}
					else if (settingAttrib.equals(saveWindowSize.getSettingName())) {
						saveWindowSize.setCustomBoolean(getSingleBooleanFromSetting(s, saveWindowSize.getDefaultBoolean(), DEFAULT_DELIMITER));
						continue;
					}
					else if (settingAttrib.equals(windowPosition.getSettingName())) {
						try {
							int xPos = getSingleIntegerFromSetting(s, (int)windowPosition.getDefaultVector2f().x, DEFAULT_DELIMITER, SEPARATOR);
							int yPos = getSingleIntegerFromSetting(s, (int)windowPosition.getDefaultVector2f().y, SEPARATOR);
							
							if (xPos <= 0 || xPos >= Window.getPrimaryMonitorWidth() || yPos <= 0 || yPos >= Window.getPrimaryMonitorHeight()) {
								windowPosition.resetVector2f();
							}
							else {
								windowPosition.setCustomVector2f(getVector2fValue(new Vector2f(xPos, yPos), windowPosition.getDefaultVector2f()));
							}
						} catch (NumberFormatException e) {
							LoggerUtil.logWarn(getClass(), e, PROGRAM_SETTINGS.getFileNameWithExtension() + " is corrupt! Did you edit this file? Unable to get correct windowPosition. Using default value instead.", true);
							windowPosition.resetVector2f();
						}
						continue;
					}
					else if (settingAttrib.equals(saveWindowPosition.getSettingName())) {
						saveWindowPosition.setCustomBoolean(getSingleBooleanFromSetting(s, saveWindowPosition.getDefaultBoolean(), DEFAULT_DELIMITER));
						continue;
					}
					else if (settingAttrib.equals(fullscreen.getSettingName())) {
						fullscreen.setCustomBoolean(getSingleBooleanFromSetting(s, fullscreen.getDefaultBoolean(), DEFAULT_DELIMITER));
						continue;
					}
					else if (settingAttrib.equals(vSync.getSettingName())) {
						vSync.setCustomBoolean(getSingleBooleanFromSetting(s, vSync.getCustomBoolean(), DEFAULT_DELIMITER));
						continue;
					}
					else if (settingAttrib.equals(fieldOfView.getSettingName())) {
						fieldOfView.setCustomInteger(getSingleIntegerFromSetting(s, fieldOfView.getDefaultInteger(), DEFAULT_DELIMITER));
					}
					else if (settingAttrib.equals(renderDistance.getSettingName())) {
						renderDistance.setCustomInteger(getSingleIntegerFromSetting(s, renderDistance.getDefaultInteger(), DEFAULT_DELIMITER));
					}
					else {
						throw new IllegalStateException(s + " is not an expected option.");
					}
				}
				
			} catch (Exception e) {
				LoggerUtil.logWarn(getClass(), e, PROGRAM_SETTINGS.getFileNameWithExtension() + " is corrupt! Using default values.", true);
			}
		}
		else {
			new File(PROGRAM_SETTINGS.getParentDirectory().getFullDirectory()).mkdirs();
		}
	}

	@Override
	protected void setDefaultSettings() {
		windowSize.resetVector2f();
		saveWindowSize.resetBoolean();
		windowPosition.resetVector2f();
		saveWindowPosition.resetBoolean();
		fullscreen.resetBoolean();
		vSync.resetBoolean();
		fieldOfView.resetInteger();
		renderDistance.resetInteger();
	}

	@Override
	public void storeSettingsInFile() {
		try (PrintStream writer = new PrintStream(PROGRAM_SETTINGS.getFullPath(), "UTF-8")) {
			writer.println(windowSize.getSettingName() + DEFAULT_DELIMITER + (int)windowSize.getCustomVector2f().x + "," + (int)windowSize.getCustomVector2f().y);
			writer.println(saveWindowSize.getSettingName() + DEFAULT_DELIMITER + saveWindowSize.getCustomBoolean());
			writer.println(windowPosition.getSettingName() + DEFAULT_DELIMITER + (int)windowPosition.getCustomVector2f().x + "," + (int)windowPosition.getCustomVector2f().y);
			writer.println(saveWindowPosition.getSettingName() + DEFAULT_DELIMITER + saveWindowPosition.getCustomBoolean());
			writer.println(fullscreen.getSettingName() + DEFAULT_DELIMITER + fullscreen.getCustomBoolean());
			writer.println(vSync.getSettingName() + DEFAULT_DELIMITER + vSync.getCustomBoolean());
			writer.println(fieldOfView.getSettingName() + DEFAULT_DELIMITER + fieldOfView.getCustomInteger());
			writer.print(renderDistance.getSettingName() + DEFAULT_DELIMITER + renderDistance.getCustomInteger());
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			LoggerUtil.logError(getClass(), e);
		}
	}
	
	/**
	 * *ALWAYS USE THIS METHOD IF YOU ARE LOOKING FOR THE CURRENT WINDOW WIDTH*
	 * Checks if the custom window size should be used. If it should be used, then it returns the custom size, else it returns the default size.
	 * @return : The current window width.
	 */
	public static int getCurrentWindowWidth() {
		if (ProgramSettings.isFullscreenEnabled()) {
			return Window.getPrimaryMonitorWidth();
		}
		else if (ProgramSettings.isWindowSizeSaved()) {
			return getSavedWindowWidth();
		}
		else {
			return getDefaultWindowWidth();
		}
	}
	
	/**
	 * *ALWAYS USE THIS METHOD IF YOU ARE LOOKING FOR THE CURRENT WINDOW HEIGHT*
	 * Checks if the custom window size should be used. If it should be used, then it returns the custom size, else it returns the default size.
	 * @return : The current window height.
	 */
	public static int getCurrentWindowHeight() {
		if (ProgramSettings.isFullscreenEnabled()) {
			return Window.getPrimaryMonitorHeight();
		}
		else if (ProgramSettings.isWindowSizeSaved()) {
			return getSavedWindowHeight();
		}
		else {
			return getDefaultWindowHeight();
		}
	}
	
	/**
	 * Updates the custom window sizes. *Does not update when going into fullscreen mode.*
	 * @param x : The window width.
	 * @param y : The window height.
	 */
	public static void updateWindowSize(int x, int y) {
		instance.windowSize.setCustomVector2f(x, y);
	}
	
	/**
	 * @return : The default window width setting.
	 */
	public static int getDefaultWindowWidth() {
		return (int)instance.windowSize.getDefaultVector2f().x;
	}
	
	/**
	 * @return : The default window height setting.
	 */
	public static int getDefaultWindowHeight() {
		return (int)instance.windowSize.getDefaultVector2f().y;
	}
	
	/**
	 * @return : The custom window width setting.
	 */
	public static int getSavedWindowWidth() {
		return (int)instance.windowSize.getCustomVector2f().x;
	}
	
	/**
	 * @return : The custom window height setting.
	 */
	public static int getSavedWindowHeight() {
		return (int)instance.windowSize.getCustomVector2f().y;
	}
	
	/**
	 * Updates the boolean that decides if the window size should be used the next time the game is opened.
	 * @param b : The boolean indicator.
	 */
	public static void updateSaveWindowSize(boolean b) {
		instance.saveWindowSize.setCustomBoolean(b);
	}
	
	/**
	 * @return : Whether or not the current window size is saved/will be used on reopening.
	 */
	public static boolean isWindowSizeSaved() {
		return instance.saveWindowSize.getCustomBoolean();
	}
	
	/**
	 * *ALWAYS USE THIS METHOD IF YOU ARE LOOKING FOR THE CURRENT WINDOW X POSITION*
	 * Checks if the custom window position should be used. If it should be used, then it returns the custom x position, else it returns the default x position.
	 * @return : The current window x position.
	 */
	public static int getCurrentWindowPositionX() {
		if (ProgramSettings.isWindowPositionSaved()) {
			return getSavedWindowPositionX();
		}
		else {
			return (Window.getPrimaryMonitorWidth() - ProgramSettings.getCurrentWindowWidth()) / 2;
		}
	}
	
	/**
	 * *ALWAYS USE THIS METHOD IF YOU ARE LOOKING FOR THE CURRENT WINDOW Y POSITION*
	 * Checks if the custom window position should be used. If it should be used, then it returns the custom y position, else it returns the default y position.
	 * @return : The current window y position.
	 */
	public static int getCurrentWindowPositionY() {
		if (ProgramSettings.isWindowPositionSaved()) {
			return getSavedWindowPositionY();
		}
		else {
			return (Window.getPrimaryMonitorHeight() - ProgramSettings.getCurrentWindowHeight()) / 2;
		}
	}
	
	/**
	 * Updates the custom window position vector.
	 * @param x : The x position of the window.
	 * @param y : The y position of the window.
	 */
	public static void updateWindowPosition(int x, int y) {
		instance.windowPosition.setCustomVector2f(x, y);
	}
	
	/**
	 * Updates the boolean that decides if the window position should be used next time the game opens.
	 * @param b : The boolean indicator.
	 */
	public static void updateSaveWindowPosition(boolean b) {
		instance.saveWindowPosition.setCustomBoolean(b);
	}
	
	/**
	 * @return : Whether or not the window position is saved.
	 */
	public static boolean isWindowPositionSaved() {
		return instance.saveWindowPosition.getCustomBoolean();
	}
	
	/**
	 * @return : The custom window x position.
	 */
	public static int getSavedWindowPositionX() {
		return (int)instance.windowPosition.getCustomVector2f().x;
	}
	
	/**
	 * @return : The custom window y position.
	 */
	public static int getSavedWindowPositionY() {
		return (int)instance.windowPosition.getCustomVector2f().y;
	}
	
	/**
	 * @return : The default window x position.
	 */
	public static int getDefaultWindowPositionX() {
		return (int)instance.windowPosition.getDefaultVector2f().x;
	}
	
	/**
	 * @return : The default window y position.
	 */
	public static int getDefaultWindowPositionY() {
		return (int)instance.windowPosition.getDefaultVector2f().y;
	}
	
	/**
	 * Toggles the fullscreen indicator between fullscreen and windowed mode.
	 * @param b : The boolean indicator.
	 */
	public static void updateFullscreen(boolean b) {
		instance.storeSettingsInFile();
		instance.fullscreen.setCustomBoolean(b);
		Window.buildScreen();
		ModelResourceManager.rebuildVAOs();
//		ControlOptions.setPaused(ControlOptions.isPaused());
		CameraMatrixManager.buildProjectionMatrix();
//		GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);//TODO:MOVE THIS
	}
	
	/**
	 * @return : Boolean indicator saying if fullscreen is enabled.
	 */
	public static boolean isFullscreenEnabled() {
		return instance.fullscreen.getCustomBoolean();
	}
	
	public static boolean isVSyncEnabled() {
		return instance.vSync.getCustomBoolean();
	}
	
	public static void updateVSync(boolean b) {
		instance.vSync.setCustomBoolean(b);
	}
	
	public static int getCustomFOV() {
		return instance.fieldOfView.getCustomInteger();
	}
	
	public static void updateFOV(int fov) {
		instance.fieldOfView.setCustomInteger(fov);
	}
	
	public static int getDefaultFOV() {
		return instance.fieldOfView.getDefaultInteger();
	}
	
	public static int getDefaultRenderDistance() {
		return instance.renderDistance.getDefaultInteger();
	}
	
	public static int getCustomRenderDistance() {
		return instance.renderDistance.getCustomInteger();
	}
	
	public static void updateRenderDistance(int renderDist) {
		instance.renderDistance.setCustomInteger(renderDist);
	}
	
	public static ProgramSettings getInstance() {
		return instance;
	}
}
