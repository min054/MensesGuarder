package com.smilevchy.life.mensesguarder.base;

import com.smilevchy.life.mensesguarder.util.SharedPreferencesManager;

import android.app.Application;

public class MensesApplication extends Application {
	
	@Override
	public void onCreate() {
		super.onCreate();
		
		SharedPreferencesManager.init(this);
	}
}
