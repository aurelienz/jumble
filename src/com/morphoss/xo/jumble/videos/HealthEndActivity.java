package com.morphoss.xo.jumble.videos;

import java.util.Locale;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.VideoView;

import com.morphoss.xo.jumble.BaseActivity;
import com.morphoss.xo.jumble.R;
import com.morphoss.xo.jumble.frontend.CategoryScreenActivity;

public class HealthEndActivity extends BaseActivity {

	private static final String TAG = "HealthEndActivity";
	public final static String EXTRA_MESSAGE = "com.morphoss.xo.jumble.MESSAGE";
	private VideoView mVideoView;

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		Locale current = this.getResources().getConfiguration().locale;
		String cc = current.getCountry();
		Log.d(TAG, "cc for music: " + cc);
		if (cc.contains("FR")) {
			myApp.playMusicVideo("level7_fr.ogg");
		} else {
			myApp.playMusicVideo("level7_en.ogg");
		}
		setContentView(R.layout.activity_end);
		mVideoView = (VideoView) findViewById(R.id.surface_view);
		mVideoView.setVideoURI(Uri.parse("android.resource://"
				+ getPackageName() + "/" + R.raw.level7));
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

	private void startCategoryActivity() {
		Intent intent = new Intent();
		intent.setClass(HealthEndActivity.this, CategoryScreenActivity.class);
		startActivity(intent);
		finish();
	}

	public void Skip_video(View view) {
		mVideoView.pause();
		mVideoView.stopPlayback();
		myApp.stopPlaying();
		startCategoryActivity();

	}

}
