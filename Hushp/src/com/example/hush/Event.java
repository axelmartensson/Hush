package com.example.hush;

import java.io.Serializable;
import java.util.Calendar;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

public class Event implements Comparable<Event>, Parcelable, Serializable{
	private static final long serialVersionUID = -2137167169541421687L;

	private Calendar startDate, endDate;

	public static final Parcelable.Creator<Event> CREATOR = new Parcelable.Creator<Event>() {
		public Event createFromParcel(Parcel in) {
			return new Event(in);
		}

		public Event[] newArray(int size) {
			return new Event[size];
		}
	};

	private Event(Parcel in) {
		Bundle b = in.readBundle();
		startDate = (Calendar) b.get("startDate");
		endDate = (Calendar) b.get("endDate");
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		Bundle b = new Bundle();
		b.putSerializable("startDate", startDate);
		b.putSerializable("endDate", endDate);
		dest.writeBundle(b);
	}

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

	@Override
	public int compareTo(Event e) {
		return startDate.compareTo(e.startDate);
	}

	public boolean equals(Object obj){
		if(obj instanceof Event){
			Event e = (Event) obj;
			return startDate.equals(e.startDate)&&endDate.equals(e.endDate);
		}
		return false;
	}
	public String toString() {
		return startDate.get(Calendar.DAY_OF_MONTH) + "";
	}

}
