package com.example.hush;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MockCalendarSynchronizer extends CalendarSynchronizer{
	private List<Event> list;

	public MockCalendarSynchronizer(List<Event> list) {
		super("");
		this.list = list;
	}

	public MockCalendarSynchronizer() {
		super("");
		list = new ArrayList<Event>();
	}
	
	public List<Event> getAllEventsFromNowUntil(Calendar endDate){
		return list;
	}

}
