package com.example.hush;

import static org.junit.Assert.*;

import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.shadows.ShadowAlarmManager;
import org.robolectric.shadows.ShadowAlarmManager.ScheduledAlarm;
import org.robolectric.shadows.ShadowPendingIntent;

import android.app.AlarmManager;
import android.content.Context;
import android.content.Intent;

@RunWith(RobolectricTestRunner.class)
public class MuteServiceTest {
	private Calendar startDate;

	@Before
	public void setUp() {
		startDate = Calendar.getInstance();
		startDate.set(Calendar.YEAR, 2014);
		startDate.set(Calendar.MONTH, 0);
		startDate.set(Calendar.DAY_OF_MONTH, 13);
		startDate.set(Calendar.HOUR_OF_DAY, 16);
		startDate.set(Calendar.MINUTE, 00);
		startDate.set(Calendar.SECOND, 0);
	}
	@Test
	public void shouldMutePhone() {

	}

	@Test
	public void shouldScheduleUnMuteOnTestActivity() {
		String className = ".UnMuteService";
		LinkedList<Event> events = new LinkedList<Event>();
		Calendar eventTime = Calendar.getInstance();
		events.add(new Event(eventTime, eventTime));

		Intent mutePhone = new Intent(Robolectric.application, MuteService.class);
		mutePhone.putExtra("events", events);
		new MuteService.Muter(Robolectric.application,
				mutePhone).execute();

		ScheduledAlarm alarm = getNextScheduledAlarm();
		Intent startUnMuteService = getIntent(alarm);
		assertEquals(className, startUnMuteService.getComponent()
				.getShortClassName());
		assertEquals(eventTime.getTimeInMillis(), alarm.triggerAtTime);

		List<Event> eventsInIntent = (List<Event>) startUnMuteService.getExtras().get("events");
		assertEquals(events, eventsInIntent);
	}

	private Intent getIntent(ScheduledAlarm alarm) {

		ShadowPendingIntent shadowPendingIntent = Robolectric
				.shadowOf(alarm.operation);
		Intent startScheduleUpdateService = shadowPendingIntent
				.getSavedIntent();
		return startScheduleUpdateService;
	}

	private ScheduledAlarm getNextScheduledAlarm() {
		AlarmManager alarmManager = (AlarmManager) Robolectric.application
				.getSystemService(Context.ALARM_SERVICE);
		ShadowAlarmManager shadowAlarmManager = Robolectric
				.shadowOf(alarmManager);
		ScheduledAlarm alarm = shadowAlarmManager.getNextScheduledAlarm();
		return alarm;
	}

}
