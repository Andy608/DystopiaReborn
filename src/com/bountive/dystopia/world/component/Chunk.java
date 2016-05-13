package com.bountive.dystopia.world.component;


public class Chunk {

	public static final int CHUNK_SIZE = 16;
	
	private int chunkX, chunkZ;
	private byte tileIDs[][];
	private EnumSafetyLevel safetyLevel;
	private EnumQuadrant quadrant;
	
	public Chunk(int x, int z) {
		tileIDs = new byte[16][16];
		chunkX = x;
		chunkZ = z;
		
		//Temporary
		for (int b = 0; b < tileIDs.length; b++) {
			for (int h = 0; h < tileIDs[b].length; h++) {
				tileIDs[b][h] = 0;
			}
		}
		safetyLevel = EnumSafetyLevel.LEVEL_1;
		quadrant = EnumQuadrant.QUADRANT_1;
	}
	
	/**
	 * Returns the X distance between chunks.
	 * If the two chunks are in the same quadrant then simple subtraction can be used. (Absolute value is used in case we subtract a smaller coordinate by a larger one).
	 * It is okay to subtract the x coordinates if the quadrants are 1 & 4 or 2 & 3 because that does not effect the x distance.
	 * 
	 * If the chunks are in quadrants 1 & 3 or 2 & 4 or 1 & 2 or 3 & 4 then we need to add the coordinates together because the coordinates cross over the axis and there are no negatives so it allows us to add.
	 * 		Because there are two zero values from crossing the axis, we need to add 1 to compensate.
	 * @param otherQuadrant : The quadrant of the other chunk.
	 * @param otherChunkZ : The z coordinate in chunk space of the other chunk.
	 * @return : The z distance between the two chunks.
	 */
	public int getDistanceX(EnumQuadrant otherQuadrant, int otherChunkX) {
		if (quadrant == otherQuadrant 
		|| (quadrant == EnumQuadrant.QUADRANT_1 && otherQuadrant == EnumQuadrant.QUADRANT_4)
		|| (quadrant == EnumQuadrant.QUADRANT_2 && otherQuadrant == EnumQuadrant.QUADRANT_3)) {
			return Math.abs(otherChunkX - chunkX);
		}
		else {
			return otherChunkX + chunkX + 1;
		}
	}
	
	/**
	 * Returns the Z distance between chunks.
	 * If the two chunks are in the same quadrant then simple subtraction can be used. (Absolute value is used in case we subtract a smaller coordinate by a larger one).
	 * It is okay to subtract the z coordinates if the quadrants are 1 & 2 or 3 & 4 because that does not effect the z distance.
	 * 
	 * If the chunks are in quadrants 1 & 3 or 2 & 4 or 1 & 4 or 2 & 3 then we need to add the coordinates together because the coordinates cross over the axis and there are no negatives so it allows us to add.
	 * 		Because there are two zero values from crossing the axis, we need to add 1 to compensate.
	 * @param otherQuadrant : The quadrant of the other chunk.
	 * @param otherChunkZ : The z coordinate in chunk space of the other chunk.
	 * @return : The z distance between the two chunks.
	 */
	public int getDistanceZ(EnumQuadrant otherQuadrant, int otherChunkZ) {
		if (quadrant == otherQuadrant
		|| (quadrant == EnumQuadrant.QUADRANT_1 && otherQuadrant == EnumQuadrant.QUADRANT_2)
		|| (quadrant == EnumQuadrant.QUADRANT_3 && otherQuadrant == EnumQuadrant.QUADRANT_4)) {
			return Math.abs(otherChunkZ - chunkZ);
		}
		else {
			return otherChunkZ + chunkZ + 1;
		}
	}
	
	/**
	 * Returns the radial distance of the desired chunk position compared to the current chunk.
	 * @param otherQuadrant : The other quadrant that the desired chunk is in.
	 * @param otherChunkX : The x coordinate in chunk space of the desired chunk.
	 * @param otherChunkZ : The z coordinate in chunk space of the desired chunk.
	 * @return : The radial distance of the desired chunk.
	 */
	public int getRadialDistance(EnumQuadrant otherQuadrant, int otherChunkX, int otherChunkZ) {
		int xDistance = getDistanceX(otherQuadrant, otherChunkX);
		int zDistance = getDistanceZ(otherQuadrant, otherChunkZ);
		
		if (xDistance > zDistance) {
			return xDistance;
		}
		return zDistance;
	}
	
	public byte getTileID(int b, int h) {
		return tileIDs[b][h];
	}
	
	public EnumSafetyLevel getSafetyLevel() {
		return safetyLevel;
	}
	
	public EnumQuadrant getQuadrant() {
		return quadrant;
	}
	
	public int getChunkX() {
		return chunkX;
	}
	
	public int getChunkZ() {
		return chunkZ;
	}
	
	public String getSaveName() {
		return new String(chunkX + "_" + chunkZ + "_chunk");
	}
	
	@Override
	public boolean equals(Object other) {
		if (other == null) return false;
		if (other instanceof Chunk) {
			Chunk otherChunk = (Chunk)other;
			if (quadrant == otherChunk.quadrant && chunkX == otherChunk.chunkX && chunkZ == otherChunk.chunkZ) {
				return true;
			}
		}
		return false;
	}
	
	public boolean isChunk(EnumQuadrant otherQuadrant, int otherChunkX, int otherChunkZ) {
		if (quadrant == otherQuadrant && chunkX == otherChunkX && chunkZ == otherChunkZ) {
			return true;
		}
		return false;
	}
	
	public enum EnumSafetyLevel {
		LEVEL_1,
		LEVEL_2,
		LEVEL_3,
		LEVEL_4,
		LEVEL_5;
	}
	
	public enum EnumQuadrant {
		QUADRANT_1,
		QUADRANT_2,
		QUADRANT_3,
		QUADRANT_4;
	}
}
