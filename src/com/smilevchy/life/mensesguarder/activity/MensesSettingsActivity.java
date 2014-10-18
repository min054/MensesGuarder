package com.smilevchy.life.mensesguarder.activity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.smilevchy.life.mensesguarder.R;
import com.smilevchy.life.mensesguarder.util.MensesGuarderUtil;
import com.smilevchy.life.mensesguarder.util.SharedPreferencesManager;
import com.smilevchy.life.mensesguarder.util.SharedPreferencesManager.SharedPreferencesKey;

public class MensesSettingsActivity extends Activity {
	public static final String TAG = "MensesSettingsActivity";
	
	private DatePicker lastMsTimeDp = null;
	private EditText periodEdit = null;
	private EditText daysToStartAlarmEdit = null;
	private TimePicker alarmTimeTP = null;
	private Button confirmBtn = null;
	private Button cancelBtn = null;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.l_activity_settings);
		
		lastMsTimeDp = (DatePicker) findViewById(R.id.lastMsTimeDP);
		periodEdit = (EditText) findViewById(R.id.periodEdit);
		daysToStartAlarmEdit = (EditText) findViewById(R.id.daysToStartAlarmEdit);
		alarmTimeTP = (TimePicker) findViewById(R.id.alarmTimeTP);
		confirmBtn = (Button) findViewById(R.id.confirm);
		cancelBtn = (Button) findViewById(R.id.cancel);
		
		init();
		configEvent();
	}
	
	private void init() {
		Calendar curCal = Calendar.getInstance();
		final int curYear = curCal.get(Calendar.YEAR);
		final int curMonth = curCal.get(Calendar.MONTH);
		final int curDay = curCal.get(Calendar.DAY_OF_MONTH);
		final int curHour = curCal.get(Calendar.HOUR_OF_DAY);
		final int curMinute = curCal.get(Calendar.MINUTE);
		
		lastMsTimeDp.init(curYear, curMonth, curDay, new OnDateChangedListener() {
			@Override
			public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
				if (year > curYear || monthOfYear > (curMonth + 1) || dayOfMonth > curDay) {
					Toast.makeText(MensesSettingsActivity.this, getString(R.string.inputError), Toast.LENGTH_SHORT).show();
					lastMsTimeDp.updateDate(curYear, curMonth, curDay);
				}
			}
		});
		
		String lastMsTimeStr = SharedPreferencesManager.getString(SharedPreferencesKey.LAST_MS_TIME, "");
		if (!TextUtils.isEmpty(lastMsTimeStr)) {
			try {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINESE);
				
				Calendar calOfLastMsTime = Calendar.getInstance();
				calOfLastMsTime.setTime(sdf.parse(lastMsTimeStr));
				
				lastMsTimeDp.updateDate(calOfLastMsTime.get(Calendar.YEAR), calOfLastMsTime.get(Calendar.MONTH), calOfLastMsTime.get(Calendar.DAY_OF_MONTH));
			} catch (ParseException e) {
				Log.e(TAG, "init() : init lastMsTimeDp error " + e.getMessage());
			}
		}
		
		String periodStr = SharedPreferencesManager.getString(SharedPreferencesKey.MS_PERIOD, "");
		if (!TextUtils.isEmpty(periodStr)) {
			periodEdit.setText(periodStr);
		}
		
		String daysToStartAlarmStr = SharedPreferencesManager.getString(SharedPreferencesKey.DAYS_TO_START_ALARM, "");
		if (!TextUtils.isEmpty(daysToStartAlarmStr)) {
			daysToStartAlarmEdit.setText(daysToStartAlarmStr);
		}
		
		alarmTimeTP.setCurrentHour(curHour);
		alarmTimeTP.setCurrentMinute(curMinute);
		
		String alarmTimeStr = SharedPreferencesManager.getString(SharedPreferencesKey.ALARM_TIME, "");
		if (!TextUtils.isEmpty(alarmTimeStr)) {
			try {
				SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.CHINESE);
				
				Calendar calOfAlarmTime = Calendar.getInstance();
				calOfAlarmTime.setTime(sdf.parse(alarmTimeStr));
				
				alarmTimeTP.setCurrentHour(calOfAlarmTime.get(Calendar.HOUR_OF_DAY));
				alarmTimeTP.setCurrentMinute(calOfAlarmTime.get(Calendar.MINUTE));
			} catch (ParseException e) {
				Log.e(TAG, "init() : init lastMsTimeDp error " + e.getMessage());
			}
		}
	}
	
	private void configEvent() {
		confirmBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (TextUtils.isEmpty(periodEdit.getText()) || TextUtils.isEmpty(daysToStartAlarmEdit.getText())) {
					Toast.makeText(MensesSettingsActivity.this, getString(R.string.inputWarningOfNum), Toast.LENGTH_SHORT).show();
					return;
				}
				
				String lastMsTime = "" + lastMsTimeDp.getYear() + "-" + (lastMsTimeDp.getMonth() + 1) + "-" + lastMsTimeDp.getDayOfMonth();
				SharedPreferencesManager.putString(SharedPreferencesKey.LAST_MS_TIME, lastMsTime);
				
				String period = periodEdit.getText().toString();
				SharedPreferencesManager.putString(SharedPreferencesKey.MS_PERIOD, period);
				
				String daysToStartAlarm = daysToStartAlarmEdit.getText().toString();
				SharedPreferencesManager.putString(SharedPreferencesKey.DAYS_TO_START_ALARM, daysToStartAlarm);
				
				String alarmTime = "" + alarmTimeTP.getCurrentHour() + ":" + alarmTimeTP.getCurrentMinute();
				SharedPreferencesManager.putString(SharedPreferencesKey.ALARM_TIME, alarmTime);
				
				SharedPreferencesManager.putBoolean(SharedPreferencesKey.IS_ALARMING, true);
				
				MensesGuarderUtil.sendAlarm(MensesSettingsActivity.this);
				
				finish();
			}
		});
		
		cancelBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
}
