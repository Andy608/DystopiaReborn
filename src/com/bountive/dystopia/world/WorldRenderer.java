package com.bountive.dystopia.world;

import java.util.List;

import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import math.Matrix4f;

import com.bountive.dystopia.camera.Camera;
import com.bountive.dystopia.camera.CameraMatrixManager;
import com.bountive.dystopia.model.util.ModelList;
import com.bountive.dystopia.model.util.ModelRaw;
import com.bountive.dystopia.shader.WorldShader;
import com.bountive.dystopia.world.component.Chunk;

public class WorldRenderer {

	//TODO:Create shader handler class that can flip between multiple shaders for different render passes.
	private WorldShader worldShader;
	private Matrix4f transformationMatrix;
	
	public WorldRenderer() {
		worldShader = new WorldShader();
		transformationMatrix = new Matrix4f();
	}
	
	public void render(World currentWorld) {
		Camera c = currentWorld.getCamera();
		
		worldShader.bind();
		worldShader.loadProjectionMatrix(CameraMatrixManager.getProjectionMatrix());
		worldShader.loadViewAndNormalMatrix(c.getViewMatrix());
		ModelRaw model = ModelList.modelTile;
		GL30.glBindVertexArray(model.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		worldShader.loadCameraPosition(c);
		
		//Draw active chunks
		transformationMatrix.setIdentity();
//		List<Chunk> activeChunks = currentWorld.worldChunkManager.getActiveChunks();
		
		GL20.glDisableVertexAttribArray(1);
		GL20.glDisableVertexAttribArray(0);
		worldShader.unbind();
	}
}
