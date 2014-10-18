package com.smilevchy.life.mensesguarder.activity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.smilevchy.life.mensesguarder.R;
import com.smilevchy.life.mensesguarder.util.MensesGuarderUtil;
import com.smilevchy.life.mensesguarder.util.SharedPreferencesManager;
import com.smilevchy.life.mensesguarder.util.SharedPreferencesManager.SharedPreferencesKey;

public class MensesMainActivity extends Activity {
	public static final String TAG = "MensexMainActivity";
	
	private TextView lastMsTimeTV = null;
	private TextView nextMsTimeTV = null;
	private TextView msPeriodTV = null;
	private Button alarmBtn = null;
	
	private String lastMsTimeStr = null;
	private String period = null;
	private String nextMsTimeStr = null;
	private boolean isAlarming = false;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.l_activity_menses_main);
		
		lastMsTimeTV = (TextView) findViewById(R.id.lastMsTimeTV);
		nextMsTimeTV = (TextView) findViewById(R.id.nextMsTimeTV);
		msPeriodTV = (TextView) findViewById(R.id.msPeriodTV);
		alarmBtn = (Button) findViewById(R.id.alarmBtn);
		
		configEvent();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		init();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu, menu);
		
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.action_settings:
				goToSetting();
				break;
			case R.id.about:
				showAbout();
				break;
			case R.id.exit:
				finish();
				break;
			default:
				break;
		}
		
		return super.onOptionsItemSelected(item);
	}
	
	private void init() {
		lastMsTimeStr = SharedPreferencesManager.getString(SharedPreferencesKey.LAST_MS_TIME, "");
		if (!TextUtils.isEmpty(lastMsTimeStr)) {
			lastMsTimeTV.setText(lastMsTimeStr);
		}
		
		period = SharedPreferencesManager.getString(SharedPreferencesKey.MS_PERIOD, "");
		if (!TextUtils.isEmpty(period)) {
			msPeriodTV.setText(period);
		}
		
		nextMsTimeStr = calculateNextMsTime();
		if (!TextUtils.isEmpty(nextMsTimeStr)) {
			nextMsTimeTV.setText(nextMsTimeStr);
		}
		
		if (TextUtils.isEmpty(lastMsTimeStr) || TextUtils.isEmpty(period) || TextUtils.isEmpty(nextMsTimeStr)) {
			alarmBtn.setText(getString(R.string.goToSetting));
		} else {
			isAlarming = SharedPreferencesManager.getBoolean(SharedPreferencesKey.IS_ALARMING, false);
			if (isAlarming) {
				alarmBtn.setText(getString(R.string.stopAlarm));
			} else {
				alarmBtn.setText(getString(R.string.startAlarm));
			}
		}
	}
	
	private void configEvent() {
		alarmBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (getString(R.string.goToSetting).equals(alarmBtn.getText())) {
					goToSetting();
				} else {
					if (isAlarming) {
						MensesGuarderUtil.cancelAlarm(MensesMainActivity.this);
						SharedPreferencesManager.putBoolean(SharedPreferencesKey.IS_ALARMING, false);
						alarmBtn.setText(getString(R.string.startAlarm));
						isAlarming = false;
					} else {
						MensesGuarderUtil.sendAlarm(MensesMainActivity.this);
						SharedPreferencesManager.putBoolean(SharedPreferencesKey.IS_ALARMING, true);
						alarmBtn.setText(getString(R.string.stopAlarm));
						isAlarming = true;
					}
				}
			}
		});
	}
	
	private String calculateNextMsTime() {
		if (!TextUtils.isEmpty(lastMsTimeStr) && !TextUtils.isEmpty(period)) {
			try {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
				
				Calendar calOfLastMsTime = Calendar.getInstance();
				calOfLastMsTime.setTime(sdf.parse(lastMsTimeStr));
				calOfLastMsTime.add(Calendar.DAY_OF_MONTH, Integer.parseInt(period));
				
				nextMsTimeStr = calOfLastMsTime.get(Calendar.YEAR) + "-" + (calOfLastMsTime.get(Calendar.MONTH) + 1) + "-" + calOfLastMsTime.get(Calendar.DAY_OF_MONTH);
				
				return nextMsTimeStr;
			} catch (ParseException e) {
				Log.e(TAG, "calculateNextMsTime() : get lastMsTimeStr error " + e.getMessage());
			}
		}
		
		return "";
	}
	
	private void goToSetting() {
		Intent intent = new Intent();
		intent.setClass(this, MensesSettingsActivity.class);
		startActivity(intent);
	}
	
	private void showAbout() {
		Toast toast = Toast.makeText(this, getString(R.string.aboutThis), Toast.LENGTH_LONG);
		toast.show();
	}
}
