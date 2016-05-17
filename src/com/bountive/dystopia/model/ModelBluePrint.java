package com.bountive.dystopia.model;

public abstract class ModelBlueprint {

	protected float[] vertexPositions;
	protected int[] indices;
	
	protected float[] vertexNormals;
	protected float[] textureCoordinates;
	
	public ModelBlueprint() {}
	
	public ModelBlueprint(float[] positions, int[] inds, float[] normals, float[] textCoords) {
		vertexPositions = positions;
		indices = inds;
		vertexNormals = normals;
		textureCoordinates = textCoords;
	}
	
	public float[] getPositions() {
		return vertexPositions;
	}
	
	public int[] getIndices() {
		return indices;
	}
	
	public float[] getVertexNormals() {
		return vertexNormals;
	}
	
	public float[] getTextureCoords() {
		return textureCoordinates;
	}
}
