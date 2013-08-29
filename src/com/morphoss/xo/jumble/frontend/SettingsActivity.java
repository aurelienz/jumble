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
package com.morphoss.xo.jumble.frontend;

import java.util.Locale;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

import com.morphoss.xo.jumble.BaseActivity;
import com.morphoss.xo.jumble.R;
import com.morphoss.xo.jumble.database.JumbleProvider;

public class SettingsActivity extends BaseActivity {

	/**
	 * This class sets the settings/options of the game
	 */
	private static String languageToLoad = "EN";
	private SeekBar volumeSeekbar = null;
	private AudioManager audioManager = null;
	private static final String TAG = "SettingsActivity";
	private Button btnreset;
	public static ContentResolver resolver;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setVolumeControlStream(AudioManager.STREAM_MUSIC);
		setContentView(R.layout.activity_settings);
		initControls();
		btnreset = (Button) findViewById(R.id.reset);
		btnreset.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				deleteWords();

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
		myApp.pauseMusic();
	}

	/**
	 * This method resets the words that are in the table words of the database
	 */
	private void deleteWords() {
		resolver = this.getContentResolver();
		resolver.delete(JumbleProvider.CONTENT_URI_WORDS, null, null);
	}

	@Override
	protected void onStart() {
		super.onStart();
		myApp.playMusic("generale.ogg");
	}

	/**
	 * This methods controls the audio
	 */
	private void initControls() {
		try {
			volumeSeekbar = (SeekBar) findViewById(R.id.seekBar1);
			audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
			volumeSeekbar.setMax(audioManager
					.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
			volumeSeekbar.setProgress(audioManager
					.getStreamVolume(AudioManager.STREAM_MUSIC));

			volumeSeekbar
					.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
						@Override
						public void onStopTrackingTouch(SeekBar bar) {
						}

						@Override
						public void onStartTrackingTouch(SeekBar bar) {
						}

						@Override
						public void onProgressChanged(SeekBar bar,
								int paramInt, boolean paramBoolean) {
							audioManager.setStreamVolume(
									AudioManager.STREAM_MUSIC, paramInt, 0);
						}
					});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * This method set English as the current language
	 * 
	 * @param v
	 */
	public void setEnglish(View v) {
		languageToLoad = "EN"; // english language
		Locale locale = new Locale(languageToLoad);
		Locale.setDefault(locale);
		Configuration config = new Configuration();
		config.locale = locale;
		getBaseContext().getResources().updateConfiguration(config,
				getBaseContext().getResources().getDisplayMetrics());
		this.setContentView(R.layout.activity_settings);
		initControls();
	}

	/**
	 * This method sets French as the current language
	 * 
	 * @param v
	 */
	public void setFrench(View v) {
		languageToLoad = "FR"; // french language
		Locale locale = new Locale(languageToLoad);
		Locale.setDefault(locale);
		Configuration config = new Configuration();
		config.locale = locale;
		getBaseContext().getResources().updateConfiguration(config,
				getBaseContext().getResources().getDisplayMetrics());
		this.setContentView(R.layout.activity_settings);
		initControls();
	}

	/**
	 * This method gets back to the MainActivity
	 * 
	 * @param v
	 */
	public void getBack(View v) {
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
		finish();
	}

	/**
	 * 
	 * @return the language selected and that should be used in the app
	 */
	public static String getLanguageToLoad() {
		return languageToLoad;
	}

	public static void setLanguageToLoad(String languageToLoad) {
		SettingsActivity.languageToLoad = languageToLoad;
	}
}
