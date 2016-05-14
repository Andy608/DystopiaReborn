package com.bountive.dystopia.world.component;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import com.bountive.dystopia.debug.logger.LoggerUtil;
import com.bountive.dystopia.file.FileResourceLocation;
import com.bountive.dystopia.file.FileResourceLocation.EnumFileExtension;
import com.bountive.dystopia.file.ResourceDirectory;

public class ChunkSaver {

	public final ResourceDirectory QUAD_1;
	public final ResourceDirectory QUAD_2;
	public final ResourceDirectory QUAD_3;
	public final ResourceDirectory QUAD_4;
	
	public ChunkSaver(ResourceDirectory worldDirectory) {
		QUAD_1 = new ResourceDirectory(worldDirectory.getFullDirectory(), "quad1", false);
		QUAD_2 = new ResourceDirectory(worldDirectory.getFullDirectory(), "quad2", false);
		QUAD_3 = new ResourceDirectory(worldDirectory.getFullDirectory(), "quad3", false);
		QUAD_4 = new ResourceDirectory(worldDirectory.getFullDirectory(), "quad4", false);
	}
	
	public void saveChunks(ArrayList<Chunk> chunks) {
		
		for (Chunk c : chunks) {
			EnumQuadrant quadrant = EnumQuadrant.getQuadrant(c.getChunkX(), c.getChunkZ());
			
			switch (quadrant) {
			case QUADRANT_1: {
				saveChunk(QUAD_1, c);
				break;
			}
			case QUADRANT_2: {
				saveChunk(QUAD_2, c);
				break;
			}
			case QUADRANT_3: {
				saveChunk(QUAD_3, c);
				break;
			}
			default: {
				saveChunk(QUAD_4, c);
			}
			}
		}
	}
	
	private void saveChunk(ResourceDirectory dir, Chunk c) {
		FileResourceLocation chunkFile = new FileResourceLocation(dir, c.getChunkX() + "_" + c.getChunkZ() + "_chunk", EnumFileExtension.DAT);

		File location = new File(chunkFile.getFullPath());
		
		if (!location.exists()) {
			new File(chunkFile.getParentDirectory().getFullDirectory()).mkdirs();
			System.out.println("MADE LOCATION DIR: " + location.getAbsolutePath());
		}
		
		try (DataOutputStream os = new DataOutputStream(new FileOutputStream(location));) {
			os.writeByte(c.getChunkX());
			os.writeByte(c.getChunkZ());
			os.writeByte(c.getSafetyLevel().ordinal());
			
			for (int z = 0; z < Chunk.CHUNK_SIZE; z++) {
				for (int x = 0; x < Chunk.CHUNK_SIZE; x++) {
					os.writeByte(c.getTileID(x, z));
				}
			}
			LoggerUtil.logInfo(getClass(), "Chunk successfully saved.");
		} catch (FileNotFoundException e) {
			if (chunkFile != null) {
				LoggerUtil.logError(getClass(), "File: " + chunkFile.getFullPath() + " could not be found.", e);
			}
			else {
				LoggerUtil.logError(getClass(), "File path is null.", e);
			}
		} catch (IOException e) {
			LoggerUtil.logError(getClass(), e);
		}
	}
}
