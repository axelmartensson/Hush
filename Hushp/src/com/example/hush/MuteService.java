package com.example.hush;

import java.util.LinkedList;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
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
		private AudioManager audioManager;
		public Muter(Context context, Intent intent){
			super(context);
			events = (LinkedList<Event>) intent.getSerializableExtra(EXTRA_NAME);
			audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
		}
		
		public void execute(){
			Event event = events.getFirst();
			mutePhone();
			scheduleUnMute(event);
		}

		private void mutePhone() {
			//TODO: get ringer mode from user settings
			int ringerMode = AudioManager.RINGER_MODE_VIBRATE;
			//----------------------------
			audioManager.setRingerMode(ringerMode);
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
