package com.example.hush;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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
	private static final String API_KEY = "AIzaSyD9bCnfIL-OsiGkBgjVDpjpbk7UaEXANfo";
	private AndroidHttpClient httpClient = AndroidHttpClient
			.newInstance("Hush");
	private SimpleDateFormat googleDateFormat = new SimpleDateFormat(
			"yyyy-mm-dd'T'HH:mm:ssXXX", Locale.US);
	private String calendarId;

	public CalendarSynchronizer(String calendarId) {
		super();
		this.calendarId = calendarId;
	}
	
	public List<Event> getAllEventsFromNowUntil(Calendar endDate)
			throws HttpResponseException {
		return getAllEventsBetween(Calendar.getInstance(), endDate);
	}

	public List<Event> getAllEventsBetween(Calendar startDate, Calendar endDate)
			throws HttpResponseException {
		List<Event> eventsBefore = new LinkedList<Event>();
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
		return eventsBefore;
	}

	private Calendar extractDate(JSONObject event, String end)
			throws JSONException {
		JSONObject start = event.getJSONObject(end);
		String dateTime = start.getString("dateTime");
		Date date = null;
		try {
			date = googleDateFormat.parse(dateTime);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar;
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
