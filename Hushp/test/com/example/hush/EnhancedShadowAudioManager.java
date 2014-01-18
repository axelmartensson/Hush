package com.example.hush;

import org.robolectric.annotation.Implements;
import org.robolectric.shadows.ShadowAudioManager;

import android.media.AudioManager;

@Implements(value=android.media.AudioManager.class)
public class EnhancedShadowAudioManager extends ShadowAudioManager {
	private int ringerMode;
	public EnhancedShadowAudioManager(){
		ringerMode = AudioManager.RINGER_MODE_NORMAL;
	}
	public int getRingerMode() {
		return ringerMode;
	}
	public void setRingerMode(int ringerMode) {
		this.ringerMode = ringerMode;
	}

}
