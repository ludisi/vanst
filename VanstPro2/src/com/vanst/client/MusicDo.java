package com.vanst.client;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;

public class MusicDo {
	static public MediaPlayer start;
	static public MediaPlayer re;
	static public MediaPlayer info;
	static public MediaPlayer error;
	static boolean sound1 = true;
	static boolean sound2 = true;
	Activity activity;
	public MusicDo(Activity activity){
		this.activity = activity;
		SharedPreferences pref=activity.getSharedPreferences("config", Context.MODE_PRIVATE);
    	sound1 = pref.getBoolean("sound1", true);
    	sound2 = pref.getBoolean("sound2", true);
		start = MediaPlayer.create(activity, R.raw.start);
		re = MediaPlayer.create(activity, R.raw.re);
		info = MediaPlayer.create(activity, R.raw.info);
		error = MediaPlayer.create(activity, R.raw.error);
	}
	public void musicStart(Activity activity){
		if(sound2){
			start = MediaPlayer.create(activity, R.raw.start);
			MediaPlayer startMediaPlayer = start;
			if(startMediaPlayer != null)
				startMediaPlayer.start();
		}
	}
	public void musicInfo(Activity activity){
		if(sound2){
			info = MediaPlayer.create(activity, R.raw.info);
			MediaPlayer infoMediaPlayer = info;
			if(infoMediaPlayer != null)
				infoMediaPlayer.start();
		}
	}
	public void musicRe(Activity activity){
		if(sound1){
			re = MediaPlayer.create(activity, R.raw.re);
			MediaPlayer reMediaPlayer = re;
			if(reMediaPlayer != null) reMediaPlayer.start();
		}
	}
	public void musicError(Activity activity){
		if(sound2){
			error = MediaPlayer.create(activity, R.raw.error);
			MediaPlayer errorMediaPlayer = error;
			if(errorMediaPlayer != null) error.start();
		}
	}
	public void musicTest(Activity activity){
		re = MediaPlayer.create(activity, R.raw.re);
		MediaPlayer reMediaPlayer = re;
		if(reMediaPlayer != null) reMediaPlayer.start();
	}
	
}
