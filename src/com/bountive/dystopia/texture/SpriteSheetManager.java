package com.bountive.dystopia.texture;

import java.util.ArrayList;
import java.util.List;

import com.bountive.dystopia.core.FileResourceTracker;
import com.bountive.dystopia.core.IRelease;
import com.bountive.dystopia.debug.logger.LoggerUtil;

public class SpriteSheetManager implements IRelease {
	
	//TODO: EXPAND ON THIS - SpriteSheetManager.class
	
	public static SpriteSheetManager instance;
	private List<SpriteSheet> activeSpriteSheets;
	
	private SpriteSheetManager() {
		activeSpriteSheets = new ArrayList<>();
	}
	
	public static void init() {
		if (instance == null) {
			instance = new SpriteSheetManager();
			FileResourceTracker.addClass(instance);
		}
		else {
			LoggerUtil.logWarn(instance.getClass(), instance.getClass().getSimpleName() + ".class has already been initialized.");
		}
	}
	
	public static void addSpriteSheet(SpriteSheet sheet) {
		instance.activeSpriteSheets.add(sheet);
	}
	
	public static SpriteSheet getSpriteSheet(String sheetName) {
		for (SpriteSheet s : instance.activeSpriteSheets) {
			if (s.getName().equalsIgnoreCase(sheetName)) {
				return s;
			}
		}
		LoggerUtil.logWarn(instance.getClass(), "Uh oh, there is no active spritesheet under the name of: " + sheetName);
		return null;
	}
	
	public static void releaseSpriteSheet(String sheetName) {
		for (int i = 0; i < instance.activeSpriteSheets.size(); i++) {
			if (instance.activeSpriteSheets.get(i).getName().equalsIgnoreCase(sheetName)) {
				instance.activeSpriteSheets.get(i).release();
				instance.activeSpriteSheets.remove(instance.activeSpriteSheets.get(i));
				break;
			}
		}
	}
	
	public static void releaseSpriteSheets() {
		LoggerUtil.logInfo(instance.getClass(), "Releasing spritesheet resources.");
		for (SpriteSheet s : instance.activeSpriteSheets) {
			s.release();
		}
	}

	@Override
	public void release() {
		releaseSpriteSheets();
	}
}
