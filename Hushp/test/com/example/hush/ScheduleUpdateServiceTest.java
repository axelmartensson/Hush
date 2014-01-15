package com.example.hush;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.shadows.ShadowAlarmManager;
import org.robolectric.shadows.ShadowAlarmManager.ScheduledAlarm;

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
//		List<Class<?>> lst = Robolectric.getDefaultShadowClasses();
//		for (Class<?> class1 : lst) {
//			System.out.println(class1);
//		}
		String responseBody = Util.fileContentToString("test/httpResponse.txt");
    	Robolectric.addPendingHttpResponse(200, responseBody);
		new ScheduleUpdateService.ScheduleUpdater(Robolectric.application).execute();
		AlarmManager alarmManager = (AlarmManager) Robolectric.application.getSystemService(Context.ALARM_SERVICE);
		ShadowAlarmManager shadow = Robolectric.shadowOf(alarmManager);
		ScheduledAlarm alarm = shadow.peekNextScheduledAlarm();
		//find a way to assert that schedulesynchronize.class is in the intent
		System.out.println(alarm.operation.toString());
		
	}
}
