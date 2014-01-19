package com.example.hush;

import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import org.apache.http.client.HttpResponseException;

import android.app.AlarmManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;

public class ScheduleUpdateService extends Service {

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		System.out.println("ScheduleUpdateService Started!!");
		// TODO FROM SETTINGS: google calendar name
		String calendarId = "student.lu.se_p8k1ctgclj9c72qete7qalh43s@group.calendar.google.com";

		Calendar nextRun = Calendar.getInstance();
		// TODO FROM SETTINGS: update Interval
		nextRun.add(Calendar.DATE, 1);

		// spawn new thread for CalendarSynchronizer
		CalendarSynchronizerTask calendarSynchronizerTask = new CalendarSynchronizerTask(
				calendarId, nextRun);

		// the execute() method is inherited from AsyncTask
			calendarSynchronizerTask.execute(0);
			
		return startId;
	}
	class CalendarSynchronizerTask extends AsyncTask<Integer, LinkedList<Event>, LinkedList<Event>>
	{

		private CalendarSynchronizer calendarSynchronizer;
		private Calendar nextRun;

		public CalendarSynchronizerTask(String calendarId, Calendar nextRun) {
			calendarSynchronizer = new CalendarSynchronizer(calendarId);
			this.nextRun = nextRun;
		}

		@Override
		protected LinkedList<Event> doInBackground(Integer... ignored) {

			try {
				return calendarSynchronizer.getAllEventsFromNowUntil(nextRun);
			} catch (HttpResponseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
		}
		
		protected void onPostExecute(LinkedList<Event> events){
			if (events == null){
				System.err.println("HTTP EXCEPTION!!");
			} else {
				new ScheduleUpdater(ScheduleUpdateService.this, events, nextRun).execute();
			}
			stopSelf();
		}
		
	}
	static class ScheduleUpdater extends AlarmScheduler {
		private LinkedList<Event> events;
		private Calendar nextRun;

		public ScheduleUpdater(Context context, LinkedList<Event> events, Calendar nextRun) {
			super(context);
			this.events = events;
			this.nextRun = nextRun;
		}

		void execute() {

			scheduleNextRun(nextRun);
			scheduleMuteOnFirstEvent(nextRun);

		}

		private void scheduleNextRun(Calendar nextRun) {
			Intent wakeMeUp = new Intent(context, ScheduleUpdateService.class);
			scheduleIntent(nextRun, wakeMeUp);
		}

		private void scheduleMuteOnFirstEvent(Calendar nextRun) {
			if (!events.isEmpty()) {
				Event first = events.get(0);
				Calendar nextMute = first.getStartDate();
				Intent mutePhone = new Intent(context, MuteService.class);
				mutePhone.putExtra(AlarmScheduler.EXTRA_NAME, events);
				scheduleIntent(nextMute, mutePhone);
			}
		}
	}

}
