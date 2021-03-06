package com.bountive.dystopia.file.setting;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

import org.lwjgl.glfw.GLFW;

import com.bountive.dystopia.debug.logger.LoggerUtil;
import com.bountive.dystopia.file.FileResourceLocation;
import com.bountive.dystopia.file.FileResourceLocation.EnumFileExtension;

public class ControlSettings extends SettingsBase {

	private static final FileResourceLocation CONTROL_SETTINGS = new FileResourceLocation(SETTINGS_DIR, "control_settings", EnumFileExtension.PROPERTIES);
	
	private static ControlSettings instance;
	
	public static MultiKeySetting shutdownKeyBinding;
	
	public static SingleKeySetting fullscreenKey;
	
	public static SingleKeySetting moveForwardKey;
	public static SingleKeySetting moveBackwardKey;
	public static SingleKeySetting moveLeftKey;
	public static SingleKeySetting moveRightKey;
	
	public static SingleKeySetting moveUpKey;
	public static SingleKeySetting moveDownKey;
	public static PercentSetting mouseSensitivity;
	
	private ControlSettings() {
		initDefaultSettings();
	}
	
	public static void init() throws IllegalStateException {
		if (instance == null) {
			instance = new ControlSettings();
		}
		else {
			LoggerUtil.logWarn(instance.getClass(), instance.getClass().getSimpleName() + ".class has already been initialized.");
		}
	}
	
	@Override
	protected void initDefaultSettings() {
		shutdownKeyBinding = new MultiKeySetting("shutdown_key_binding", GLFW.GLFW_KEY_F1, GLFW.GLFW_KEY_ESCAPE);//TEMP
		fullscreenKey = new SingleKeySetting("fullscreen_key", GLFW.GLFW_KEY_F2);
		moveForwardKey = new SingleKeySetting("move_forward_key", GLFW.GLFW_KEY_W);
		moveBackwardKey = new SingleKeySetting("move_backward_key", GLFW.GLFW_KEY_S);
		moveLeftKey = new SingleKeySetting("move_left_key", GLFW.GLFW_KEY_D);
		moveRightKey = new SingleKeySetting("move_right_key", GLFW.GLFW_KEY_A);
		moveUpKey = new SingleKeySetting("jump_key", GLFW.GLFW_KEY_SPACE);
		moveDownKey = new SingleKeySetting("duck_key", GLFW.GLFW_KEY_LEFT_SHIFT);
		mouseSensitivity = new PercentSetting("mouse_sensitivity_percentage", 4, 100f, 26f, 200f);
	}

	@Override
	public void loadSettingsFromFile() {
		File location = new File(CONTROL_SETTINGS.getFullPath());
		setDefaultSettings();
		
		if (location.exists()) {
			try {
				String[] settings = readOptionsFile(location);
				
				for (String s : settings) {
					String controlAttrib = s.substring(0, s.indexOf(DEFAULT_DELIMITER));
					
					if (controlAttrib.equals(shutdownKeyBinding.getSettingName())) {
						shutdownKeyBinding.setCustomKeyBinding(getMultipleIntegersFromSetting(s, shutdownKeyBinding.getDefaultKeyBinding(), DEFAULT_DELIMITER, SEPARATOR));
						continue;
					}
					else if (controlAttrib.equals(fullscreenKey.getSettingName())) {
						fullscreenKey.setCustomKey(getSingleIntegerFromSetting(s, fullscreenKey.getCustomKey(), DEFAULT_DELIMITER));
						continue;
					}
					else if (controlAttrib.equals(moveForwardKey.getSettingName())) {
						moveForwardKey.setCustomKey(getSingleIntegerFromSetting(s, moveForwardKey.getCustomKey(), DEFAULT_DELIMITER));
						continue;
					}
					else if (controlAttrib.equals(moveBackwardKey.getSettingName())) {
						moveBackwardKey.setCustomKey(getSingleIntegerFromSetting(s, moveBackwardKey.getDefaultKey(), DEFAULT_DELIMITER));
						continue;
					}
					else if (controlAttrib.equals(moveLeftKey.getSettingName())) {
						moveLeftKey.setCustomKey(getSingleIntegerFromSetting(s, moveLeftKey.getDefaultKey(), DEFAULT_DELIMITER));
						continue;
					}
					else if (controlAttrib.equals(moveRightKey.getSettingName())) {
						moveRightKey.setCustomKey(getSingleIntegerFromSetting(s, moveRightKey.getDefaultKey(), DEFAULT_DELIMITER));
						continue;
					}
					else if (controlAttrib.equals(moveUpKey.getSettingName())) {
						moveUpKey.setCustomKey(getSingleIntegerFromSetting(s, moveUpKey.getDefaultKey(), DEFAULT_DELIMITER));
						continue;
					}
					else if (controlAttrib.equals(moveDownKey.getSettingName())) {
						moveDownKey.setCustomKey(getSingleIntegerFromSetting(s, moveDownKey.getDefaultKey(), DEFAULT_DELIMITER));
						continue;
					}
					else if (controlAttrib.equals(mouseSensitivity.getSettingName())) {
						mouseSensitivity.setCustomPercent(getSingleFloatFromSetting(s, mouseSensitivity.getDefaultPercent(), DEFAULT_DELIMITER));
						continue;
					}
					else {
						throw new IllegalStateException(s + " is not an expected setting.");
					}
				}
			} catch (Exception e) {
				LoggerUtil.logWarn(getClass(), e, CONTROL_SETTINGS.getFileNameWithExtension() + " is corrupt! Using default values.", true);
			}
		}
		else {
			new File(CONTROL_SETTINGS.getParentDirectory().getFullDirectory()).mkdirs();
		}
	}

	@Override
	protected void setDefaultSettings() {
		shutdownKeyBinding.resetKeyBinding();
		moveForwardKey.resetKey();
		moveBackwardKey.resetKey();
		moveLeftKey.resetKey();
		moveRightKey.resetKey();
	}

	@Override
	public void storeSettingsInFile() {
		try (PrintStream writer = new PrintStream(CONTROL_SETTINGS.getFullPath(), "UTF-8")) {
			writer.println(shutdownKeyBinding.getSettingName() + DEFAULT_DELIMITER + shutdownKeyBinding.getReadableCustomKeyBinding());
			writer.println(fullscreenKey.getSettingName() + DEFAULT_DELIMITER + fullscreenKey.getCustomKey());
			writer.println(moveForwardKey.getSettingName() + DEFAULT_DELIMITER + moveForwardKey.getCustomKey());
			writer.println(moveBackwardKey.getSettingName() + DEFAULT_DELIMITER + moveBackwardKey.getCustomKey());
			writer.println(moveLeftKey.getSettingName() + DEFAULT_DELIMITER + moveLeftKey.getCustomKey());
			writer.println(moveRightKey.getSettingName() + DEFAULT_DELIMITER + moveRightKey.getCustomKey());
			writer.println(moveUpKey.getSettingName() + DEFAULT_DELIMITER + moveUpKey.getCustomKey());
			writer.println(moveDownKey.getSettingName() + DEFAULT_DELIMITER + moveDownKey.getCustomKey());
			writer.print(mouseSensitivity.getSettingName() + DEFAULT_DELIMITER + mouseSensitivity.getCustomPercent());
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			LoggerUtil.logError(getClass(), e);
		}
	}
	
	public static ControlSettings getInstance() {
		return instance;
	}
}