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
package com.morphoss.jumble.videos;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.VideoView;

import com.morphoss.jumble.BaseActivity;
import com.morphoss.jumble.frontend.AvatarActivity;
import com.morphoss.jumble.frontend.MainActivity;
import com.morphoss.jumble.frontend.SettingsActivity;
import com.morphoss.jumble.R;

public class StoryActivity extends BaseActivity {

	/**
	 * This class displays the video at the beginning of the game
	 */
	private static final String TAG = "StoryActivity";
	public final static String EXTRA_MESSAGE = "com.morphoss.xo.jumble.MESSAGE";
	private VideoView mVideoView;

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		String cc = SettingsActivity.getLanguageToLoad();
		Log.d(TAG, "cc for music: " + cc);
		if (cc.contains("fr")) {
			myApp.playMusicVideo("story_fr.ogg");
		} else {
			myApp.playMusicVideo("story_en.ogg");
		}
		setContentView(R.layout.activity_story);
		mVideoView = (VideoView) findViewById(R.id.surface_view);
		mVideoView.setVideoURI(Uri.parse("android.resource://"
				+ getPackageName() + "/" + R.raw.beginning));
		mVideoView.setMediaController(null);
		mVideoView.requestFocus();
		mVideoView.start();
		mVideoView
				.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
					@Override
					public void onCompletion(MediaPlayer mp) {
						// Video Playing is completed
						startAvatarActivity();

					}
				});

	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
		finish();
	}

	/**
	 * This method starts the AvatarActivity
	 */
	private void startAvatarActivity() {
		myApp.stopPlaying();
		Intent intent = new Intent();
		intent.setClass(StoryActivity.this, AvatarActivity.class);
		startActivity(intent);
		finish();
	}

	/**
	 * This methods allows to skip the video and go directly to the
	 * AvatarActivity
	 * 
	 * @param view
	 */
	public void Skip_video(View view) {
		mVideoView.pause();
		mVideoView.stopPlayback();
		startAvatarActivity();
	}
}
