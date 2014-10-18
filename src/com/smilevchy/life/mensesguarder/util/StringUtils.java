package com.smilevchy.life.mensesguarder.util;

public class StringUtils {
	
	public static String pad(int c) {
		if (c >= 10) {
			return String.valueOf(c);
		} else {
			return "0" + String.valueOf(c);
		}
	}
}
