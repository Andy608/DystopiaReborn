package com.bountive.dystopia.world.generation;

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
	
	public static int convertXBasedOnQuadrant(EnumQuadrant quadrant, int xValue, int divisor) {
		if (quadrant == EnumQuadrant.QUADRANT_1 || quadrant == EnumQuadrant.QUADRANT_4) {
			return xValue / divisor;
		}
		else {
			return (xValue - divisor) / divisor;
		}
	}
	
	public static int convertZBasedOnQuadrant(EnumQuadrant quadrant, int zValue, int divisor) {
		if (quadrant == EnumQuadrant.QUADRANT_1 || quadrant == EnumQuadrant.QUADRANT_2) {
			return zValue / divisor;
		}
		else {
			return (zValue - divisor) / divisor;
		}
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
