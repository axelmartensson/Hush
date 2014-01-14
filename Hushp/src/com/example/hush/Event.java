package com.example.hush;

import java.util.Calendar;

public class Event {
	private Calendar startDate, endDate;

	public Event(Calendar startDate, Calendar endDate) {
		super();
		this.startDate = startDate;
		this.endDate = endDate;
	}

	public Calendar getStartDate() {
		return startDate;
	}

	public Calendar getEndDate() {
		return endDate;
	}
}
