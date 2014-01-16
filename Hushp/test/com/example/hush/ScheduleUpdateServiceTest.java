package com.example.hush;

import static org.junit.Assert.*;

import java.util.List;

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
public class ScheduleUpdateServiceTest {
	@Test
	public void shouldNotFail() {
	  Object systemService = 
	    Robolectric.application.getSystemService(
	      Context.ALARM_SERVICE);

	  assertFalse(systemService == null);
	}
	
	@Test
	public void shouldScheduleItself(){
		String className = ".ScheduleUpdateService";
		MockCalendarSynchronizer mockCalendarSynchronizer = new MockCalendarSynchronizer();
		new ScheduleUpdateService.ScheduleUpdater(Robolectric.application, mockCalendarSynchronizer).execute();
		AlarmManager alarmManager = (AlarmManager) Robolectric.application.getSystemService(Context.ALARM_SERVICE);
		ShadowAlarmManager shadowAlarmManager = Robolectric.shadowOf(alarmManager);
		ScheduledAlarm alarm = shadowAlarmManager.peekNextScheduledAlarm();
		ShadowPendingIntent shadowPendingIntent = Robolectric.shadowOf(alarm.operation);
		Intent startScheduleUpdateService = shadowPendingIntent.getSavedIntent();
		assertEquals(className, startScheduleUpdateService.getComponent().getShortClassName());
	}

	@Test
	public void shouldScheduleTestActivity(){
	}
}
