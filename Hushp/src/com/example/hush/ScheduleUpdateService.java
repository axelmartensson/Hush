package com.example.hush;

import java.util.Calendar;
import java.util.List;

import org.apache.http.client.HttpResponseException;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

public class ScheduleUpdateService extends Service {

	private static final int REQUEST_CODE = 420;

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}
	
	@Override
	public int onStartCommand (Intent intent, int flags, int startId){
		AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
		Calendar nextRun = Calendar.getInstance();
		//TODO FROM SETTINGS: update Interval
		nextRun.add(Calendar.DATE, 1);
		Intent wakeMeUp = new Intent(this, ScheduleUpdateService.class);
		PendingIntent wakeMeUpLater = PendingIntent.getService(this, REQUEST_CODE, wakeMeUp, PendingIntent.FLAG_UPDATE_CURRENT);
		alarmManager.set(AlarmManager.RTC_WAKEUP, nextRun.getTimeInMillis(), wakeMeUpLater);
		
		setAlarmOnEventsUntil(nextRun, alarmManager);
		
		stopSelf();
		return startId;
		
	}

	private void setAlarmOnEventsUntil(Calendar nextRun, AlarmManager alarmManager) {
		//TODO FROM SETTINGS: google calendar name
		String calendarId = "student.lu.se_p8k1ctgclj9c72qete7qalh43s@group.calendar.google.com";
		//spawn new thread for CalendarSynchronizer
		CalendarSynchronizer calendarSynchronizer = new CalendarSynchronizer(calendarId);
		
		try {
			List<Event> events = calendarSynchronizer.getAllEventsFromNowUntil(nextRun);
			for (Event event : events) {
				//set alarm on event.startDate and event.endDate for Mute and Unmunte
			}
		} catch (HttpResponseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}