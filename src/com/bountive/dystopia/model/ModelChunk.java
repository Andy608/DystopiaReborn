package com.bountive.dystopia.model;

import com.bountive.dystopia.model.util.ModelRaw;
import com.bountive.dystopia.model.util.VBOWrapper;
import com.bountive.dystopia.world.generation.Chunk;

public class ModelChunk extends ModelRaw {

	private Chunk chunk;
	
	public ModelChunk(Chunk c, int arrayID, int[] ind, VBOWrapper[] bufferIDs) {
		super(arrayID, ind, bufferIDs);
		chunk = c;
	}
	
	public Chunk getChunk() {
		return chunk;
	}
}
