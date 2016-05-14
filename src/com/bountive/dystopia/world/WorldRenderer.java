package com.bountive.dystopia.world;

import java.util.ArrayList;

import math.Matrix4f;
import math.Vector3f;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import com.bountive.dystopia.camera.Camera;
import com.bountive.dystopia.camera.CameraMatrixManager;
import com.bountive.dystopia.math.MatrixMathHelper;
import com.bountive.dystopia.model.util.ModelList;
import com.bountive.dystopia.model.util.ModelRaw;
import com.bountive.dystopia.shader.WorldShader;
import com.bountive.dystopia.world.component.Chunk;

public class WorldRenderer {

	//TODO:Create shader handler class that can flip between multiple shaders for different render passes.
	private WorldShader worldShader;
	private Matrix4f transformationMatrix;
	private Vector3f translation;
	
	public WorldRenderer() {
		worldShader = new WorldShader();
		transformationMatrix = new Matrix4f();
		translation = new Vector3f();
	}
	
	public void render(World currentWorld) {
		Camera camera = currentWorld.getCamera();
		
		worldShader.bind();
		worldShader.loadProjectionMatrix(CameraMatrixManager.getProjectionMatrix());
		worldShader.loadViewAndNormalMatrix(camera.getViewMatrix());
		ModelRaw model = ModelList.modelTile;
		GL30.glBindVertexArray(model.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		worldShader.loadCameraPosition(camera);
		
		//Draw active chunks
		ArrayList<Chunk> activeChunks = currentWorld.getChunkManager().getActiveChunks();
		
		for (Chunk chunk : activeChunks) {
			renderChunk(chunk, model);
		}
		
		GL20.glDisableVertexAttribArray(1);
		GL20.glDisableVertexAttribArray(0);
		worldShader.unbind();
	}
	
	//TODO: make the whole chunk a model to render at once instead of rendering each tile separately.
	private void renderChunk(Chunk chunk, ModelRaw model/*This model parameter is temporary*/) {
//		transformationMatrix.setIdentity();
//		translation.set(chunk.getChunkX(), 0, chunk.getChunkZ());
//		MatrixMathHelper.translateMatrix(transformationMatrix, translation);
//		worldShader.loadTransformationMatrix(transformationMatrix);
//		GL11.glDrawElements(GL11.GL_TRIANGLES, model.getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
	
		for (int z = 0; z < Chunk.CHUNK_SIZE; z++) {
			for (int x = 0; x < Chunk.CHUNK_SIZE; x++) {
				transformationMatrix.setIdentity();
				translation.set((chunk.getChunkX() * Chunk.CHUNK_SIZE) + x, 0, (chunk.getChunkZ() * Chunk.CHUNK_SIZE) + z);
				MatrixMathHelper.translateMatrix(transformationMatrix, translation);
				worldShader.loadTransformationMatrix(transformationMatrix);
				GL11.glDrawElements(GL11.GL_TRIANGLES, model.getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
			}
		}
	
	
	
	}
}
