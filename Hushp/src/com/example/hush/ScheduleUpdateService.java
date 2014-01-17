package com.example.hush;

import java.util.Calendar;
import java.util.LinkedList;
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
		CalendarSynchronizer calendarSynchronizer = new CalendarSynchronizer(
				calendarId);
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

		void execute() {
			Calendar nextRun = Calendar.getInstance();
			// TODO FROM SETTINGS: update Interval
			nextRun.add(Calendar.DATE, 1);
			AlarmManager alarmManager = (AlarmManager) context
					.getSystemService(Context.ALARM_SERVICE);
			scheduleNextRun(nextRun, alarmManager);
			scheduleMuteOnFirstEvent(nextRun, alarmManager);

		}

		private void scheduleNextRun(Calendar nextRun, AlarmManager alarmManager) {
			Intent wakeMeUp = new Intent(context, ScheduleUpdateService.class);
			scheduleIntent(alarmManager, nextRun, wakeMeUp);
		}

		private void scheduleMuteOnFirstEvent(Calendar nextRun,
				AlarmManager alarmManager) {
			try {
				LinkedList<Event> events = calendarSynchronizer
						.getAllEventsFromNowUntil(nextRun);
				if (!events.isEmpty()) {
					Event first = events.get(0);
					Calendar nextMute = first.getStartDate();
					Intent mutePhone = new Intent(context,
							MuteService.class);
					mutePhone.putExtra("events", events);
					scheduleIntent(alarmManager, nextMute, mutePhone);
				}
			} catch (HttpResponseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		private void scheduleIntent(AlarmManager alarmManager,
				Calendar date, Intent intent) {
			PendingIntent mutePhoneLater = PendingIntent.getService(
					context, REQUEST_CODE, intent,
					PendingIntent.FLAG_UPDATE_CURRENT);
			alarmManager.set(AlarmManager.RTC_WAKEUP,
					date.getTimeInMillis(), mutePhoneLater);
		}
	}

}
