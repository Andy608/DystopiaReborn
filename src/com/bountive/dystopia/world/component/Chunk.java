package com.bountive.dystopia.world.component;


public class Chunk {

	public static final int CHUNK_SIZE = 16;
	
	private int chunkX, chunkZ;
	private byte tileIDs[][];
	private EnumSafetyLevel safetyLevel;
	private EnumQuadrant quadrant;
	
	public Chunk(int x, int z) {
		tileIDs = new byte[CHUNK_SIZE][CHUNK_SIZE];
		chunkX = x;
		chunkZ = z;
		
		//Temporary
		for (int b = 0; b < tileIDs.length; b++) {
			for (int h = 0; h < tileIDs[b].length; h++) {
				tileIDs[b][h] = 0;
			}
		}
		safetyLevel = EnumSafetyLevel.LEVEL_1;//TODO: CHANGE THIS
		quadrant = EnumQuadrant.getQuadrant(x, z);
	}
	
	public Chunk(int x, int z, EnumSafetyLevel level, byte[][] tiles) {
		chunkX = x;
		chunkZ = z;
		quadrant = EnumQuadrant.getQuadrant(x, z);
		safetyLevel = level;
		tileIDs = tiles;
	}
	
	public int getDistanceX(int otherChunkX) {
		return Math.abs(otherChunkX - chunkX);
	}
	
	public int getDistanceZ(int otherChunkZ) {
		return Math.abs(otherChunkZ - chunkZ);
	}
	
	/**
	 * Returns the radial distance of the desired chunk position compared to the current chunk.
	 * @param otherQuadrant : The other quadrant that the desired chunk is in.
	 * @param otherChunkX : The x coordinate in chunk space of the desired chunk.
	 * @param otherChunkZ : The z coordinate in chunk space of the desired chunk.
	 * @return : The radial distance of the desired chunk.
	 */
	public int getRadialDistance(EnumQuadrant otherQuadrant, int otherChunkX, int otherChunkZ) {
		int xDistance = getDistanceX(otherChunkX);
		int zDistance = getDistanceZ(otherChunkZ);
		
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
}
