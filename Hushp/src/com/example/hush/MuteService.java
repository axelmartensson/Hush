package com.example.hush;

import java.util.LinkedList;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

public class MuteService extends Service {

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		new Muter(this, intent).execute();
		stopSelf();
		return startId;
	}
	
	static class Muter extends AlarmScheduler{
		private LinkedList<Event> events;
		public Muter(Context context, Intent intent){
			super(context);
			events = (LinkedList<Event>) intent.getSerializableExtra(EXTRA_NAME);
		}
		
		public void execute(){
			Event event = events.getFirst();
			scheduleUnMute(event);
		}

		private void scheduleUnMute(Event event) {
			Intent unMutePhone = new Intent(context, UnMuteService.class);
			unMutePhone.putExtra(EXTRA_NAME, events);
			scheduleIntent(event.getEndDate(), unMutePhone);
		}
	}
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

}
