package com.bountive.dystopia.world.component;

public enum EnumQuadrant {
	
	QUADRANT_1,
	QUADRANT_2,
	QUADRANT_3,
	QUADRANT_4;
	
	public static EnumQuadrant getQuadrant(int x, int z) {
		if (x >= 0 && z >= 0) {
			return EnumQuadrant.QUADRANT_1;
		}
		else if (x < 0 && z >= 0) {
			return EnumQuadrant.QUADRANT_2;
		}
		else if (x < 0 && z < 0) {
			return EnumQuadrant.QUADRANT_3;
		}
		else {
			return EnumQuadrant.QUADRANT_4;
		}
	}
}
