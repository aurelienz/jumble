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
import android.view.View;
import android.widget.VideoView;

import com.morphoss.jumble.BaseActivity;
import com.morphoss.jumble.frontend.MainActivity;
import com.morphoss.jumble.frontend.RulesActivity;
import com.morphoss.jumble.R;

public class RulesVideoActivity extends BaseActivity {

	/**
	 * This class displays the video for the RulesActivity
	 */
	private static final String TAG = "RulesVideoActivity";
	public final static String EXTRA_MESSAGE = "com.morphoss.xo.jumble.MESSAGE";
	private VideoView mVideoView;

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);

		setContentView(R.layout.activity_rules_video);
		mVideoView = (VideoView) findViewById(R.id.surface_view);
		mVideoView.setVideoURI(Uri.parse("android.resource://"
				+ getPackageName() + "/" + R.raw.rules));
		mVideoView.setMediaController(null);
		mVideoView.requestFocus();
		mVideoView.start();
		mVideoView
				.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
					@Override
					public void onCompletion(MediaPlayer mp) {
						// Video Playing is completed
						startRulesActivity();
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

	@Override
	protected void onPause() {
		super.onPause();
	}

	/**
	 * This method starts the RulesActivity
	 */
	private void startRulesActivity() {
		Intent intent = new Intent();
		intent.setClass(RulesVideoActivity.this, RulesActivity.class);
		startActivity(intent);
		finish();
	}

	/**
	 * This methods allows to skip the video and go directly to the
	 * RulesActivity
	 * 
	 * @param view
	 */
	public void Skip_video(View view) {
		mVideoView.pause();
		mVideoView.stopPlayback();
		startRulesActivity();

	}
}
