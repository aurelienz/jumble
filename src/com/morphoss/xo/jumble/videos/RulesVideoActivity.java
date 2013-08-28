package com.morphoss.xo.jumble.videos;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.VideoView;

import com.morphoss.xo.jumble.BaseActivity;
import com.morphoss.xo.jumble.R;
import com.morphoss.xo.jumble.frontend.AvatarActivity;
import com.morphoss.xo.jumble.frontend.CategoryScreenActivity;
import com.morphoss.xo.jumble.frontend.MainActivity;
import com.morphoss.xo.jumble.frontend.RulesActivity;
import com.morphoss.xo.jumble.frontend.SettingsActivity;

public class RulesVideoActivity extends BaseActivity {

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

	private void startRulesActivity() {
		Intent intent = new Intent();
		intent.setClass(RulesVideoActivity.this, RulesActivity.class);
		startActivity(intent);
		finish();
	}

	public void Skip_video(View view) {
		mVideoView.pause();
		mVideoView.stopPlayback();
		startRulesActivity();

	}
}
