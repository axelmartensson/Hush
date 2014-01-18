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
	
	static class UnMuter extends AlarmScheduler{
		private LinkedList<Event> events;
		public UnMuter(Context context, Intent intent){
			super(context);
			events = (LinkedList<Event>) intent.getSerializableExtra(EXTRA_NAME);
		}
		
		public void execute(){
			Event event = events.getFirst();

		}
	}
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

}
