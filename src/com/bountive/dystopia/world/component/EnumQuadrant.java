package com.bountive.dystopia.world.component;

public enum EnumQuadrant {
	
	QUADRANT_1("quad1"),
	QUADRANT_2("quad2"),
	QUADRANT_3("quad3"),
	QUADRANT_4("quad4");
	
	private String quadrantName;
	
	private EnumQuadrant(String name) {
		quadrantName = name;
	}
	
	@Override
	public String toString() {
		return quadrantName;
	}
	
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
