package com.example.hush;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.List;

import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.apache.http.client.HttpResponseException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.*;
import static org.junit.Assert.assertThat;

@RunWith(RobolectricTestRunner.class)
public class CalendarSynchronizeTest {

	private CalendarSynchronizer calendarSynchronizer;
	private Calendar lowerBoundCalendar;
	@Before
	public void setUp(){
		calendarSynchronizer = new CalendarSynchronizer("student.lu.se_p8k1ctgclj9c72qete7qalh43s@group.calendar.google.com");
		lowerBoundCalendar = Calendar.getInstance();
		lowerBoundCalendar.set(Calendar.YEAR, 2014);
		lowerBoundCalendar.set(Calendar.MONTH, 0);
		lowerBoundCalendar.set(Calendar.DAY_OF_MONTH, 13);
		lowerBoundCalendar.set(Calendar.HOUR_OF_DAY, 16);
		lowerBoundCalendar.set(Calendar.MINUTE, 00);
		lowerBoundCalendar.set(Calendar.SECOND, 0);
	}
    
    @Test
    public void shouldReturnAListOfEvents() throws Exception {
    	String responseBody = Util.fileContentToString("test/httpResponse.txt");
    	Robolectric.addPendingHttpResponse(200, responseBody);
    	Calendar calendar = Calendar.getInstance();
    	calendar.add(Calendar.DAY_OF_MONTH, 2);
    	List<Event> list = calendarSynchronizer.getAllEventsBetween(lowerBoundCalendar, calendar);
        assertFalse(list.isEmpty());
    }
    
    @Test(expected=HttpResponseException.class)
    public void shouldFailIf404() throws Exception {
    	Robolectric.addPendingHttpResponse(404, "");
    	Calendar calendar = Calendar.getInstance();
    	List<Event> list = calendarSynchronizer.getAllEventsBetween(lowerBoundCalendar, calendar);
    }
    
    @After
    public void tearDown(){
    	calendarSynchronizer = null;
    }
}