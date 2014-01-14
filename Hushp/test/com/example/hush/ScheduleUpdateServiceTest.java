package com.example.hush;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

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
	public void shouldWakeUp(){
		Intent startIntent = new Intent(Robolectric.application, ScheduleUpdateService.class);
		ScheduleUpdateService scheduleUpdateService = new ScheduleUpdateService();
		scheduleUpdateService.onStartCommand(startIntent, 0, 2);
		
	}
}
