package com.smilevchy.life.mensesguarder.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.smilevchy.life.mensesguarder.receiver.MensesBroadcastReceiver;
import com.smilevchy.life.mensesguarder.util.SharedPreferencesManager.SharedPreferencesKey;

public class MensesGuarderUtil {
	
	public static void cancelAlarm(Context context) {
		PendingIntent pi = PendingIntent.getBroadcast(context, 0, new Intent(context, MensesBroadcastReceiver.class), 0);
		
		AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		alarmManager.cancel(pi);
	}
	
	public static void sendAlarm(Context context) {
		String lastMsTime = SharedPreferencesManager.getString(SharedPreferencesKey.LAST_MS_TIME, "");
		if (TextUtils.isEmpty(lastMsTime)) {
			return;
		}
		
		String period = SharedPreferencesManager.getString(SharedPreferencesKey.MS_PERIOD, "");
		if (TextUtils.isEmpty(period)) {
			return;
		}

		String daysToStartAlarm = SharedPreferencesManager.getString(SharedPreferencesKey.DAYS_TO_START_ALARM, "");
		if (TextUtils.isEmpty(daysToStartAlarm)) {
			return;
		}

		String alarmTime = SharedPreferencesManager.getString(SharedPreferencesKey.ALARM_TIME, "");
		if (TextUtils.isEmpty(alarmTime)) {
			return;
		}
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
		
		Calendar cal = Calendar.getInstance();
		try {
			cal.setTime(sdf.parse(lastMsTime));
		} catch (ParseException e) {
			return;
		}
		
		cal.add(Calendar.DAY_OF_MONTH, Integer.parseInt(period));
		cal.add(Calendar.DAY_OF_MONTH, Integer.parseInt(daysToStartAlarm) * -1);
		
		sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
		Calendar otherCal = Calendar.getInstance();
		try {
			otherCal.setTime(sdf.parse(alarmTime));
		} catch (ParseException e) {
			return;
		}
		
		cal.set(Calendar.HOUR_OF_DAY, otherCal.get(Calendar.HOUR_OF_DAY));
		cal.set(Calendar.MINUTE, otherCal.get(Calendar.MINUTE));
		
		Intent intent = new Intent(context, MensesBroadcastReceiver.class);
		PendingIntent pi = PendingIntent.getBroadcast(context, 0, intent, 0);
	
		AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), 24 * 60 * 60 * 1000, pi);
	}
}
