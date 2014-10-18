package com.smilevchy.life.mensesguarder.receiver;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;

import com.smilevchy.life.mensesguarder.R;
import com.smilevchy.life.mensesguarder.activity.MensesMainActivity;
import com.smilevchy.life.mensesguarder.util.MensesGuarderUtil;
import com.smilevchy.life.mensesguarder.util.SharedPreferencesManager;
import com.smilevchy.life.mensesguarder.util.SharedPreferencesManager.SharedPreferencesKey;

public class MensesBroadcastReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		String lastMsTime = SharedPreferencesManager.getString(SharedPreferencesKey.LAST_MS_TIME, "");
		if (TextUtils.isEmpty(lastMsTime)) {
			return;
		}
		
		String period = SharedPreferencesManager.getString(SharedPreferencesKey.MS_PERIOD, "");
		if (TextUtils.isEmpty(period)) {
			return;
		}
		
		Calendar oldCal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
		try {
			oldCal.setTime(sdf.parse(lastMsTime));
		} catch (ParseException e) {
			return;
		}
		oldCal.add(Calendar.DAY_OF_MONTH, Integer.parseInt(period));
		
		Calendar curCal = Calendar.getInstance();
		
		if (oldCal.get(Calendar.YEAR) == curCal.get(Calendar.YEAR) && 
			oldCal.get(Calendar.MONTH) == curCal.get(Calendar.MONTH) &&
			oldCal.get(Calendar.DAY_OF_MONTH) == curCal.get(Calendar.DAY_OF_MONTH)) {
			MensesGuarderUtil.cancelAlarm(context);	
			
			lastMsTime = oldCal.get(Calendar.YEAR) + "-" + (oldCal.get(Calendar.MONTH) + 1) + "-" + oldCal.get(Calendar.DAY_OF_MONTH);
			SharedPreferencesManager.putString(SharedPreferencesKey.LAST_MS_TIME, lastMsTime);

			MensesGuarderUtil.sendAlarm(context);
		} else {
			NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
			.setSmallIcon(R.drawable.ic_launcher)
			.setContentTitle(context.getString(R.string.app_name))
			.setContentText(context.getString(R.string.mensesIsComing))
			.setAutoCancel(true)
			.setLights(Color.GREEN, 1, 0);
			
			PendingIntent pi = PendingIntent.getActivity(context, 0, new Intent(context, MensesMainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);
			builder.setContentIntent(pi);
			Notification notification = builder.build();
			
			((NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE)).notify(0, notification);
		}
	}
}
