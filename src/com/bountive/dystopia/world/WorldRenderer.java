package com.bountive.dystopia.world;

import java.util.ArrayList;

import math.Matrix4f;
import math.Vector3f;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import com.bountive.dystopia.camera.Camera;
import com.bountive.dystopia.camera.CameraMatrixManager;
import com.bountive.dystopia.math.MatrixMathHelper;
import com.bountive.dystopia.model.ModelChunk;
import com.bountive.dystopia.shader.WorldShader;
import com.bountive.dystopia.texture.TilesetList;
import com.bountive.dystopia.world.generation.Chunk;

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
		
		//Get a list of the current active chunks in the world.
		//Get the models from each active chunk. (Either build model on chunk update or build model before rendering.) //TODO: Test both ways.
		//In a for loop renderer each chunk model.
		
		//Draw active chunks
		worldShader.loadCameraPosition(camera);
		
		synchronized(this) {
			ArrayList<Chunk> activeChunks = currentWorld.getChunkManager().getActiveChunks();
			
			for (int i = 0; i < activeChunks.size(); i++) {
				Chunk chunk = activeChunks.get(i);
				
				if (chunk.getModel() == null) {
					chunk.buildModel();
				}
				ModelChunk chunkModel = chunk.getModel();
				
				GL30.glBindVertexArray(chunkModel.getVaoID());
				GL20.glEnableVertexAttribArray(0);
				GL20.glEnableVertexAttribArray(1);
				GL20.glEnableVertexAttribArray(3);
				
				GL13.glActiveTexture(GL13.GL_TEXTURE0);
				
				//TODO: Move this inside for loop when there are multiple tilesheets images.
				GL11.glBindTexture(GL11.GL_TEXTURE_2D, TilesetList.tilesetSpriteSheet.getID());
				
				transformationMatrix.setIdentity();
				translation.set(chunk.getChunkX() * Chunk.CHUNK_SIZE, 0, chunk.getChunkZ() * Chunk.CHUNK_SIZE);
				MatrixMathHelper.translateMatrix(transformationMatrix, translation);
				worldShader.loadTransformationMatrix(transformationMatrix);
				GL11.glDrawElements(GL11.GL_TRIANGLES, chunkModel.getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
			}
		}
		
		GL20.glDisableVertexAttribArray(3);
		GL20.glDisableVertexAttribArray(1);
		GL20.glDisableVertexAttribArray(0);
		worldShader.unbind();
	}
}
