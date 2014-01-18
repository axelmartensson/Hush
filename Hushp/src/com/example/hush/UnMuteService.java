package com.example.hush;

import java.util.LinkedList;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

public class UnMuteService extends Service {

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		new UnMuter(this, intent).execute();
		stopSelf();
		return startId;
	}

	static class UnMuter extends AlarmScheduler {
		private LinkedList<Event> events;

		public UnMuter(Context context, Intent intent) {
			super(context);
			events = (LinkedList<Event>) intent
					.getSerializableExtra(EXTRA_NAME);
		}

		public void execute() {
			Event event = events.removeFirst();
			unMutePhone(event);
			if (!events.isEmpty()) {
				Event nextEvent = events.getFirst();
				scheduleMute(nextEvent);
			}
		}

		private void unMutePhone(Event event) {
			// TODO Auto-generated method stub
		}

		private void scheduleMute(Event event) {
			Intent mutePhone = new Intent(context, MuteService.class);
			mutePhone.putExtra(EXTRA_NAME, events);
			scheduleIntent(event.getEndDate(), mutePhone);
		}
	}

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

}
