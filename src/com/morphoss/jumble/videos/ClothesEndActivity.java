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

import java.util.Locale;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.VideoView;

import com.morphoss.jumble.BaseActivity;
import com.morphoss.jumble.frontend.CategoryScreenActivity;
import com.morphoss.jumble.R;

public class ClothesEndActivity extends BaseActivity {

	/**
	 * This class displays the video at the end of the category Clothes
	 */
	private static final String TAG = "ClothesEndActivity";
	public final static String EXTRA_MESSAGE = "com.morphoss.xo.jumble.MESSAGE";
	private VideoView mVideoView;

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		Locale current = this.getResources().getConfiguration().locale;
		String cc = current.getCountry();
		Log.d(TAG, "cc for music: " + cc);
		if (cc.contains("fr")) {
			myApp.playMusicVideo("level6_fr.ogg");
		} else {
			myApp.playMusicVideo("level6_en.ogg");
		}
		setContentView(R.layout.activity_end);
		mVideoView = (VideoView) findViewById(R.id.surface_view);
		mVideoView.setVideoURI(Uri.parse("android.resource://"
				+ getPackageName() + "/" + R.raw.level6));
		mVideoView.setMediaController(null);
		mVideoView.requestFocus();
		mVideoView.start();
		mVideoView
				.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
					@Override
					public void onCompletion(MediaPlayer mp) {
						// Video Playing is completed
						myApp.stopPlaying();
						startCategoryActivity();
					}
				});

	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		Intent intent = new Intent(this, CategoryScreenActivity.class);
		startActivity(intent);
		finish();
	}

	/**
	 * This method starts the CategoryScreenActivity
	 */
	private void startCategoryActivity() {
		Intent intent = new Intent();
		intent.setClass(ClothesEndActivity.this, CategoryScreenActivity.class);
		startActivity(intent);
		finish();
	}

	/**
	 * This methods allows to skip the video and go directly to the
	 * CategoryScreenActivity
	 * 
	 * @param view
	 */
	public void Skip_video(View view) {
		mVideoView.pause();
		mVideoView.stopPlayback();
		myApp.stopPlaying();
		startCategoryActivity();

	}

}
