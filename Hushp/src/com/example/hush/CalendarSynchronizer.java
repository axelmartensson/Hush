package com.example.hush;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.methods.HttpGet;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.net.http.AndroidHttpClient;

public class CalendarSynchronizer {
	private static final String JAVA6FORMAT = "yyyy-mm-dd'T'HH:mm:ssZ";
	private static final String JAVA7FORMAT = "yyyy-mm-dd'T'HH:mm:ssXXXXX";
	private static final String API_KEY = "AIzaSyD9bCnfIL-OsiGkBgjVDpjpbk7UaEXANfo";
	private AndroidHttpClient httpClient = AndroidHttpClient
			.newInstance("Hush");
	private SimpleDateFormat googleDateFormat = new SimpleDateFormat(
			JAVA6FORMAT, Locale.US);
	private String calendarId;

	public CalendarSynchronizer(String calendarId) {
		super();
		this.calendarId = calendarId;
	}
	
	/**
	 * post: events are sorted in ascending order based on start date
	 * @param endDate
	 * @return a list of events in ascending order
	 * @throws HttpResponseException
	 */
	public LinkedList<Event> getAllEventsFromNowUntil(Calendar endDate)
			throws HttpResponseException {
		return getAllEventsBetween(Calendar.getInstance(), endDate);
	}

	public LinkedList<Event> getAllEventsBetween(Calendar startDate, Calendar endDate)
			throws HttpResponseException {
		LinkedList<Event> eventsBefore = new LinkedList<Event>();
		String JSONString = getJSONFromServer(startDate, endDate);
		try {
			JSONObject responseBody = new JSONObject(
					new JSONTokener(JSONString));
			JSONArray events = responseBody.getJSONArray("items");
			for (int i = 0; i < events.length(); i++) {
				JSONObject event = events.getJSONObject(i);
				Calendar eventStartDate = extractDate(event, "start");
				Calendar eventEndDate = extractDate(event, "end");
				if(eventStartDate.after(startDate)){
					eventsBefore.add(new Event(eventStartDate, eventEndDate));
				}

			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Collections.sort(eventsBefore);
		return eventsBefore;
	}

	private Calendar extractDate(JSONObject event, String end)
			throws JSONException {
		JSONObject start = event.getJSONObject(end);
		String dateTime = start.getString("dateTime");
		try {
			Date date = googleDateFormat.parse(insertColon(dateTime));
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			return calendar;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
	}
	/**
	 * This is a fix for Java 1.6, because the X format character
	 * is not available in its SimpleDateFormat implementation
	 * @param dateTime
	 * @return
	 */
	private String insertColon(String dateTime) {
		StringBuffer fixed = new StringBuffer(dateTime);
		fixed.deleteCharAt(fixed.length()-3);
		return fixed.toString();
	}

	private String getJSONFromServer(Calendar startDate, Calendar endDate) throws HttpResponseException {
		HttpResponse response = getHttpResponse(startDate, endDate);
		StatusLine statusLine = response.getStatusLine();
		int statusCode = statusLine.getStatusCode();
		if (statusCode < 200 || statusCode > 299) {
			if (statusCode == 404) {
				throw new HttpResponseException(statusCode, statusCode
						+ " - Calendar Not Found");
			} else {
				throw new HttpResponseException(statusCode, statusCode
						+ " - Unknown Error");
			}
		}
		return extractResponseBody(response);
	}

	private String extractResponseBody(HttpResponse response) {
		String responseBody = "";
		try {
			InputStream rawJSONStream = response.getEntity().getContent();
			responseBody = inputStreamToString(rawJSONStream);
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return responseBody;
	}

	private HttpResponse getHttpResponse(Calendar startDate, Calendar endDate) {
		String lowerBound = googleDateFormat.format(startDate.getTime());
		String upperBound = googleDateFormat.format(endDate.getTime());
		String uri = "https://www.googleapis.com/calendar/v3/calendars/"
				+ calendarId + "/events?timeMin="+lowerBound+"&timeMax="+upperBound+"&key=" + API_KEY;
		
		HttpGet request = new HttpGet(uri);
		HttpResponse response = null;
		try {
			response = httpClient.execute(request);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return response;
	}

	private String inputStreamToString(InputStream JSONStream) {
		StringBuilder content = new StringBuilder();
		BufferedReader buf = new BufferedReader(new InputStreamReader(
				JSONStream));
		String line = "";
		try {
			while ((line = buf.readLine()) != null) {
				content.append(line);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return content.toString();
	}
}
