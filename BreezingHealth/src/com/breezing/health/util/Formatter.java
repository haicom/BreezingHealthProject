package com.breezing.health.util;




public class Formatter {

	public static double round(double value, int places) {
		double c = 10.0;
		for(int i = 1; i < places; i++) {
			c*=10;
		}
		double x = 0.5;
		if(value < 0) {			
			x = -0.5;
		}
		return ((int)((value * c) + x))/c;
	}
}
