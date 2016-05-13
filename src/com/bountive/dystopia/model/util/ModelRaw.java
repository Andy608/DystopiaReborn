package com.bountive.dystopia.model.util;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

public class ModelRaw {

	private int vaoID;
	private int vertexCount;
	
	private int[] indices;
	private VBOWrapper[] vboIDs;
	
	public ModelRaw(int arrayID, int[] ind, VBOWrapper[] bufferIDs) {
		vaoID = arrayID;
		vertexCount = ind.length;
		indices = ind;
		vboIDs = bufferIDs;
	}
	
	public void rebuildVAO() {
		vaoID = GL30.glGenVertexArrays();
		GL30.glBindVertexArray(vaoID);
		
		for (int i = 0; i < vboIDs.length; i++) {
			GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboIDs[i].getID());
			GL20.glVertexAttribPointer(i, vboIDs[i].getVBOLength(), GL11.GL_FLOAT, false, 0, 0);
			GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
			GL20.glEnableVertexAttribArray(i);
		}
		
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, GL15.glGenBuffers());
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, BufferHelper.toReadableIntBuffer(indices), GL15.GL_STATIC_DRAW);
	}
	
	public int getVaoID() {
		return vaoID;
	}
	
	public VBOWrapper[] getVBOs() {
		return vboIDs;
	}
	
	public int getVertexCount() {
		return vertexCount;
	}
}
