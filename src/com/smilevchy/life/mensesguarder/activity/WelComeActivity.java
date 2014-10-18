package com.smilevchy.life.mensesguarder.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.view.Window;

import com.smilevchy.life.mensesguarder.R;

public class WelComeActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		setContentView(R.layout.l_activity_welcome);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		Handler handler = new Handler();
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				Intent intent = new Intent(WelComeActivity.this, MensesMainActivity.class);
				startActivity(intent);
				finish();
			}
		}, 1000);
	}
	
	@Override
	public void onBackPressed() {
		
	}
}
