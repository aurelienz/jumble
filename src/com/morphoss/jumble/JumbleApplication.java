/*
 * Copyright (C) 2013 Morphoss Ltd
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package com.morphoss.jumble;

import java.io.IOException;

import com.morphoss.jumble.R;

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

	/**
	 * This method permits to display a music from the assets folder
	 * 
	 * @param fileName
	 */
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

	/**
	 * This method permits to display a music from the assets folder
	 * 
	 * @param fileName
	 */
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

	/**
	 * This method permits to display the pronunciation of a word
	 * 
	 * @param fileName
	 */
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

	/**
	 * This method permits to stop playing the music
	 */
	public void stopPlaying() {
		player.stop();
		paused = false;

	}

	/**
	 * This method permits to pause at any time the music
	 */
	public void pauseMusic() {
		timePause = player.getCurrentPosition();
		player.pause();
		paused = true;

	}

	/**
	 * This method permits to restart the music where it was paused
	 */
	public void resumeMusic() {
		if (!paused)
			return;
		player.seekTo(timePause);
		player.start();
		paused = false;

	}
}
