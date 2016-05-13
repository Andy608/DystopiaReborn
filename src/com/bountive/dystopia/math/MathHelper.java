package com.bountive.dystopia.math;

import java.util.Random;

public class MathHelper {

	private static float[] SIN_TABLE = new float[65536];
	public static final Random RAND = new Random();
	
	public static final int clampInt(int value, int min, int max) {
		return (value > max) ? max : (value < min) ? min : value; 
	}
	
	public static final float clampFloat(float value, float min, float max) {
		return (value > max) ? max : (value < min) ? min : value;
	}
	
	public static final float sin(float degrees) {
        return SIN_TABLE[(int)(Math.toRadians(degrees) * 10430.378F) & 65535];
    }

    public static final float cos(float degrees) {
    	if (degrees == 90 || degrees == 270) return 0f;
    	else return SIN_TABLE[(int)(Math.toRadians(degrees) * 10430.378F + 16384.0F) & 65535];
    }
	
	static {
		for (int i = 0; i < SIN_TABLE.length; i++) {
            SIN_TABLE[i] = (float)Math.sin((double)i * Math.PI * 2.0D / 65536.0D);
        }
	}
}
