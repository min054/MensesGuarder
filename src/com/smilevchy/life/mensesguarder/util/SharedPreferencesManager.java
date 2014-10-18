package com.smilevchy.life.mensesguarder.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SharedPreferencesManager {
	private static final String MENSES_SHARED_PREFS = "mensesSharedPrefs";

	private static SharedPreferences sharedPrefs = null;

	
	public static void init(Context ctx) {
		if (null == sharedPrefs) {
			sharedPrefs = ctx.getSharedPreferences(MENSES_SHARED_PREFS, Context.MODE_PRIVATE);
		}
	}

	public static String getString(String key, String defValue) {
		return sharedPrefs.getString(key, defValue);
	}

	public static boolean putString(String key, String value) {
		Editor editor = sharedPrefs.edit();
		editor.putString(key, value);
		
		return editor.commit();
	}
	
	public static Long getLong(String key, Long defValue) {
		return sharedPrefs.getLong(key, defValue);
	}
	
	public static boolean putLong(String key, Long value) {
		Editor editor = sharedPrefs.edit();
		editor.putLong(key, value);
		
		return editor.commit();
	}
	
	public static boolean getBoolean(String key, boolean defValue) {
		return sharedPrefs.getBoolean(key, defValue);
	}
	
	public static boolean putBoolean(String key, boolean value) {
		Editor editor = sharedPrefs.edit();
		editor.putBoolean(key, value);
		
		return editor.commit();
	}
	
	public static final class SharedPreferencesKey {
		public static final String LAST_MS_TIME = "LAST_MS_TIME";
		public static final String MS_PERIOD = "MS_PERIOD";
		public static final String DAYS_TO_START_ALARM = "DAYS_TO_START_ALARM";
		public static final String ALARM_TIME = "ALARM_TIME";
		public static final String IS_ALARMING = "IS_ALARMING";
	}
}
