package com.bountive.dystopia.model;

import com.bountive.dystopia.model.util.ModelBuilder;
import com.bountive.dystopia.tile.Tile;
import com.bountive.dystopia.tile.TileList;
import com.bountive.dystopia.world.generation.Chunk;

public class BlueprintChunk extends ModelBlueprint {

	public ModelChunk createModel(Chunk chunk) {
		vertexPositions = new float[Tile.TILE_BLUEPRINT.vertexPositions.length * Chunk.CHUNK_SIZE * Chunk.CHUNK_SIZE];
		vertexNormals = new float[Tile.TILE_BLUEPRINT.vertexNormals.length * Chunk.CHUNK_SIZE * Chunk.CHUNK_SIZE];
		textureCoordinates = new float[Tile.TILE_BLUEPRINT.textureCoordinates.length * Chunk.CHUNK_SIZE * Chunk.CHUNK_SIZE];
		indices = new int[Tile.TILE_BLUEPRINT.indices.length * Chunk.CHUNK_SIZE * Chunk.CHUNK_SIZE];
		
		int offset = 0;
		
		for (int z = 0; z < Chunk.CHUNK_SIZE; z++) {
			for (int x = 0; x < Chunk.CHUNK_SIZE; x++) {
				ModelTile tile = TileList.getTileRegistry().getTileByID(chunk.getTileID(x, z)).getModel();
				
				for (int i = 0; i < Tile.TILE_BLUEPRINT.vertexPositions.length; i++) {
					float position;
					float normal;
					int mod = i % 3;
					
					if (mod == 0) {
						position = Tile.TILE_BLUEPRINT.vertexPositions[i] + z;
						normal = Tile.TILE_BLUEPRINT.vertexNormals[i] + z;
					}
					else if (mod == 2) {
						position = Tile.TILE_BLUEPRINT.vertexPositions[i] + x;
						normal = Tile.TILE_BLUEPRINT.vertexNormals[i] + x;
					}
					else {
						position = Tile.TILE_BLUEPRINT.vertexPositions[i];
						normal = Tile.TILE_BLUEPRINT.vertexNormals[i];
					}
					
					vertexPositions[i + (offset * Tile.TILE_BLUEPRINT.vertexPositions.length)] = position;
					vertexNormals[i + (offset * Tile.TILE_BLUEPRINT.vertexNormals.length)] = normal;
				}
				
				for (int i = 0; i < Tile.TILE_BLUEPRINT.textureCoordinates.length; i++) {
					textureCoordinates[i + (offset * Tile.TILE_BLUEPRINT.textureCoordinates.length)] = tile.getTextureCoordsVBO().getData()[i];
				}
				
				for (int i = 0; i < Tile.TILE_BLUEPRINT.indices.length; i++) {
					indices[i + (offset * Tile.TILE_BLUEPRINT.indices.length)] = Tile.TILE_BLUEPRINT.indices[i] + (offset * (Tile.TILE_BLUEPRINT.vertexPositions.length / 3));
				}
				offset++;
			}
		}
		return ModelBuilder.buildChunkModel(this, chunk);
	}
}
