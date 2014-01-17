package com.example.hush;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

public class MuteService extends Service {

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		new Muter(this).execute();
		stopSelf();
		return startId;
	}
	
	static class Muter extends AlarmScheduler{
		public Muter(Context context){
			super(context);
		}
		
		public void execute(){
			//TODO implement
		}
	}
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

}
