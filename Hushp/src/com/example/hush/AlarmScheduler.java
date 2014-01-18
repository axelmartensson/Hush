package com.example.hush;

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

public class AlarmScheduler {

	protected Context context;
	protected AlarmManager alarmManager;
	private static final int REQUEST_CODE = 420;
	protected static final String EXTRA_NAME = "events";

	public AlarmScheduler(Context context) {
		this.context = context;
		alarmManager = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
	}

	protected void scheduleIntent(Calendar date, Intent intent) {
		PendingIntent mutePhoneLater = PendingIntent.getService(context,
				REQUEST_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		alarmManager.set(AlarmManager.RTC_WAKEUP, date.getTimeInMillis(),
				mutePhoneLater);
	}

}
