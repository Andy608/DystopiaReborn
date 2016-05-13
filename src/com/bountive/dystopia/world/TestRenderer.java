package com.bountive.dystopia.world;

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

public class TestRenderer {

	private WorldShader shader;
	private Matrix4f transformationMatrix;
	
	public TestRenderer() {
		shader = new WorldShader();
		transformationMatrix = new Matrix4f();
		translation = new Vector3f();
	}
	
	private Vector3f translation;
	
//	shader.bind();
//	shader.loadProjectionMatrix(CameraMatrixManager.getProjectionMatrix());
//	shader.loadViewAndNormalMatrix(camera.getViewMatrix());
//	GL30.glBindVertexArray(tile.getModel().getVaoID());
//	GL20.glEnableVertexAttribArray(0);
//	GL20.glEnableVertexAttribArray(1);
//	transformationMatrix.setIdentity();
//	MatrixMathHelper.translateMatrix(transformationMatrix, tile.getPosition());
//	MatrixMathHelper.rotateMatrix(transformationMatrix, tile.getRotation());
//	shader.loadTransformationMatrix(transformationMatrix);
//	shader.loadCameraPosition(camera);
//	GL11.glDrawElements(GL11.GL_TRIANGLES, tile.getModel().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
//	GL20.glDisableVertexAttribArray(1);
//	GL20.glDisableVertexAttribArray(0);
//	shader.unbind();
	
	//In the future this will take in a world and go from there.
	public void render(Camera c) {
		shader.bind();
		shader.loadProjectionMatrix(CameraMatrixManager.getProjectionMatrix());
		shader.loadViewAndNormalMatrix(c.getViewMatrix());
		
		ModelRaw model = ModelList.modelTile;
		GL30.glBindVertexArray(model.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		shader.loadCameraPosition(c);
		
		for (int i = 0; i < 100; i++) {
			for (int j = 0; j < 100; j++) {
				transformationMatrix.setIdentity();
				translation.set(i, 0, j);
				MatrixMathHelper.translateMatrix(transformationMatrix, translation);
				shader.loadTransformationMatrix(transformationMatrix);
				GL11.glDrawElements(GL11.GL_TRIANGLES, model.getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
			}
		}
		
		GL20.glDisableVertexAttribArray(1);
		GL20.glDisableVertexAttribArray(0);
		shader.unbind();
	}
	
	//TODO: EXPAND ON THIS - TestRenderer.class
	public void render(World currentWorld) {
		//Use the world to get the tiles at a certain position.
		//Go through a switch statement and depending on the tile, render that image (lets start out with color).
		
		Camera c = currentWorld.getCamera();
		shader.bind();
		shader.loadProjectionMatrix(CameraMatrixManager.getProjectionMatrix());
		shader.loadViewAndNormalMatrix(c.getViewMatrix());
		
		ModelRaw model = ModelList.modelTile;
		GL30.glBindVertexArray(model.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		shader.loadCameraPosition(c);
		
		for (int i = 0; i < 100; i++) {
			for (int j = 0; j < 100; j++) {
				transformationMatrix.setIdentity();
				translation.set(i, 0, j);
				MatrixMathHelper.translateMatrix(transformationMatrix, translation);
				shader.loadTransformationMatrix(transformationMatrix);
				GL11.glDrawElements(GL11.GL_TRIANGLES, model.getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
			}
		}
		
		GL20.glDisableVertexAttribArray(1);
		GL20.glDisableVertexAttribArray(0);
		shader.unbind();
	}
}
