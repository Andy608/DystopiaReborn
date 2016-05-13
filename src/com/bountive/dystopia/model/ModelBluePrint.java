package com.bountive.dystopia.model;

public abstract class ModelBluePrint {

	private float[] vertexPositions;
	private int[] indices;
	
	private float[] vertexNormals;
	private float[] textureCoordinates;
	
	public ModelBluePrint(float[] positions, int[] inds, float[] normals, float[] textCoords) {
		vertexPositions = positions;
		indices = inds;
		vertexNormals = normals;
		textureCoordinates = textCoords;
	}
	
	public float[] getPositions() {
		return vertexPositions.clone();
	}
	
	public int[] getIndices() {
		return indices.clone();
	}
	
	public float[] getVertexNormals() {
		return vertexNormals.clone();
	}
	
	public float[] getTextureCoords() {
		return textureCoordinates.clone();
	}
}
