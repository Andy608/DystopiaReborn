package com.bountive.dystopia.model.util;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL30;

import com.bountive.dystopia.core.FileResourceTracker;
import com.bountive.dystopia.core.IRelease;
import com.bountive.dystopia.debug.logger.LoggerUtil;

public class ModelResourceManager implements IRelease {

	private static ModelResourceManager instance;
	public List<ModelRaw> activeModels;
	
	private ModelResourceManager() {
		activeModels = new ArrayList<>();
	}
	
	public static void init() {
		if (instance == null || instance.activeModels == null) {
			instance = new ModelResourceManager();
			FileResourceTracker.addClass(instance);
		}
		else {
			LoggerUtil.logWarn(instance.getClass(), instance.getClass().getSimpleName() + ".class has already been initialized.");
		}
	}
	
	public static void addModel(ModelRaw model) {
		System.out.println("ADDING MODEL");
		instance.activeModels.add(model);
	}
	
	public static void rebuildVAOs() {
		instance.release();
		
		System.out.println("REBUILDING VAOs and VBOs");
		
		for (ModelRaw model : instance.activeModels) {
			model.rebuildVAO();
		}
	}
	
	public static ModelResourceManager getInstance() {
		return instance;
	}
	
	@Override
	public void release() {
		LoggerUtil.logInfo(getClass(), "Releasing VAOs and VBOs.");

		for (ModelRaw model : instance.activeModels) {
			GL30.glDeleteVertexArrays(model.getVaoID());
		}
		
		for (ModelRaw model : instance.activeModels) {
			for (VBOWrapper vboID : model.getVBOs()) {
				GL30.glDeleteVertexArrays(vboID.getID());
			}
		}
	}
}
