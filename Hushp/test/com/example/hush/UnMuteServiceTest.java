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
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowAlarmManager;
import org.robolectric.shadows.ShadowAlarmManager.ScheduledAlarm;
import org.robolectric.shadows.ShadowPendingIntent;

import android.app.AlarmManager;
import android.content.Context;
import android.content.Intent;

@Config(shadows=EnhancedShadowAudioManager.class)
@RunWith(RobolectricTestRunner.class)
public class UnMuteServiceTest {
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
	public void shouldUnMutePhone() {

	}

	@Test
	public void shouldScheduleMuteOnNextActivity() {
		String className = ".MuteService";

		LinkedList<Event> events = new LinkedList<Event>();
		Calendar firstEventTime = Calendar.getInstance();
		Calendar secondEventTime = Calendar.getInstance();
		secondEventTime.add(Calendar.HOUR, 1);
		events.add(new Event(firstEventTime, firstEventTime));
		Event secondEvent = new Event(secondEventTime, secondEventTime);
		events.add(secondEvent);

		Intent unMutePhone = new Intent(Robolectric.application,
				MuteService.class);
		unMutePhone.putExtra("events", events);
		new UnMuteService.UnMuter(Robolectric.application, unMutePhone)
				.execute();

		ScheduledAlarm alarm = getNextScheduledAlarm();
		Intent startUnMuteService = getIntent(alarm);
		assertEquals(className, startUnMuteService.getComponent()
				.getShortClassName());
		assertEquals(secondEventTime.getTimeInMillis(), alarm.triggerAtTime);

		LinkedList<Event> eventsInIntent = (LinkedList<Event>) startUnMuteService
				.getExtras().get("events");
		assertTrue(eventsInIntent.size() == 1);
		assertEquals(secondEvent, eventsInIntent.getFirst());
	}

	@Test
	public void shouldStopSchedulingWhenNoMoreEvents() {
		String className = ".MuteService";

		LinkedList<Event> events = new LinkedList<Event>();
		Calendar firstEventTime = Calendar.getInstance();
		events.add(new Event(firstEventTime, firstEventTime));
		Intent unMutePhone = new Intent(Robolectric.application,
				MuteService.class);
		unMutePhone.putExtra("events", events);
		new UnMuteService.UnMuter(Robolectric.application, unMutePhone)
				.execute();

		ScheduledAlarm alarm = getNextScheduledAlarm();
		assertNull(alarm);
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
