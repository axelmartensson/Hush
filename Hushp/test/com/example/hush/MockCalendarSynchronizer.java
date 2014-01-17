package com.example.hush;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

public class MockCalendarSynchronizer extends CalendarSynchronizer{
	private LinkedList<Event> list;

	public MockCalendarSynchronizer(LinkedList<Event> list) {
		super("");
		this.list = list;
	}

	public MockCalendarSynchronizer() {
		super("");
		list = new LinkedList<Event>();
	}
	
	public LinkedList<Event> getAllEventsFromNowUntil(Calendar endDate){
		return list;
	}

}
