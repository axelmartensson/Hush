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
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO FROM SETTINGS: google calendar name
		String calendarId = "student.lu.se_p8k1ctgclj9c72qete7qalh43s@group.calendar.google.com";
		// spawn new thread for CalendarSynchronizer
		CalendarSynchronizer calendarSynchronizer = new CalendarSynchronizer(calendarId);
		new ScheduleUpdater(this, calendarSynchronizer).execute();
		stopSelf();
		return startId;
	}
	
	static class ScheduleUpdater {
		private Context context;
		private CalendarSynchronizer calendarSynchronizer;
		

		public ScheduleUpdater(Context context,
				CalendarSynchronizer calendarSynchronizer) {
			this.context = context;
			this.calendarSynchronizer = calendarSynchronizer;
		}

		void execute(){
			Calendar nextRun = Calendar.getInstance();
			// TODO FROM SETTINGS: update Interval
			nextRun.add(Calendar.DATE, 1);
			AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
			scheduleNextRun(nextRun, alarmManager);
			scheduleMutesOnEventsUntil(nextRun, alarmManager);

		}

		private void scheduleNextRun(Calendar nextRun, AlarmManager alarmManager) {
			Intent wakeMeUp = new Intent(context, ScheduleUpdateService.class);
			PendingIntent wakeMeUpLater = PendingIntent.getService(context,
					REQUEST_CODE, wakeMeUp, PendingIntent.FLAG_UPDATE_CURRENT);
			alarmManager.set(AlarmManager.RTC_WAKEUP,
					nextRun.getTimeInMillis(), wakeMeUpLater);
		}

		private void scheduleMutesOnEventsUntil(Calendar nextRun,
				AlarmManager alarmManager) {
			try {
				List<Event> events = calendarSynchronizer
						.getAllEventsFromNowUntil(nextRun);
				for (Event event : events) {
					// set alarm on event.startDate and event.endDate for Mute
					// and Unmunte respectively
				}
			} catch (HttpResponseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
