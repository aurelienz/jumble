package com.morphoss.xo.jumble;

import java.io.IOException;

import android.app.Application;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.util.Log;

public class JumbleApplication extends Application implements
		OnPreparedListener {
	private MediaPlayer player;
	private int timePause;
	private boolean paused = false;
	private static final String TAG = "JumbleApplication";

	@Override
	public void onCreate() {
		super.onCreate();
		player = MediaPlayer.create(this, R.raw.mute);
		player.setLooping(true);
		player.start();
		player.setOnPreparedListener(this);
	}

	public void playMusic(String fileName) {
		player.reset();
		paused = false;
		try {
			AssetFileDescriptor descriptor = getAssets().openFd(
					"music/" + fileName);
			player.setDataSource(descriptor.getFileDescriptor(),
					descriptor.getStartOffset(), descriptor.getLength());
			descriptor.close();
			player.prepare();
			player.setLooping(true);
			player.start();
		} catch (Exception e) {
			Log.w(TAG, Log.getStackTraceString(e));
		}

	}

	public void playMusicVideo(String fileName) {
		player.reset();
		paused = false;
		try {
			AssetFileDescriptor descriptor = getAssets().openFd(
					"music/" + fileName);
			player.setDataSource(descriptor.getFileDescriptor(),
					descriptor.getStartOffset(), descriptor.getLength());
			descriptor.close();
			player.prepare();
			player.setLooping(false);
			player.start();
		} catch (Exception e) {
			Log.w(TAG, Log.getStackTraceString(e));
		}

	}

	public void playSoundJumble(String fileName) {
		player.reset();
		paused = false;
		try {
			player.setDataSource(fileName);
			player.prepare();
			player.setLooping(false);
			player.start();
		} catch (Exception e) {
			Log.w(TAG, Log.getStackTraceString(e));
		}

	}

	@Override
	public void onPrepared(MediaPlayer arg0) {
		player.start();

	}

	public void stopPlaying() {
		player.stop();
		paused = false;

	}

	public void pauseMusic() {
		timePause = player.getCurrentPosition();
		player.pause();
		paused = true;

	}

	public void resumeMusic() {
		if (!paused)
			return;
		player.seekTo(timePause);
		player.start();
		paused = false;

	}
}
