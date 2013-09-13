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
package com.morphoss.jumble.frontend;

import java.util.Locale;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

import com.morphoss.jumble.BaseActivity;
import com.morphoss.jumble.R;
import com.morphoss.jumble.database.JumbleProvider;

public class SettingsActivity extends BaseActivity {

	/**
	 * This class sets the settings/options of the game
	 */
	private static String languageToLoad = null;
	private SeekBar volumeSeekbar = null;
	private AudioManager audioManager = null;
	private static final String TAG = "SettingsActivity";
	private Button btnreset;
	private ImageView enFlag;
	private ImageView frFlag;
	public static ContentResolver resolver;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setLanguage();
		initControls();

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
		Log.d(TAG, "the words have been deleted");
		resolver = this.getContentResolver();
		resolver.delete(JumbleProvider.CONTENT_URI_WORDS, null, null);
	}
	private void deleteKnownCategories() {
		Log.d(TAG, "the unlocked categories have been deleted");
		resolver = this.getContentResolver();
		resolver.delete(JumbleProvider.CONTENT_URI_CATEGORIES, null, null);
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
		setVolumeControlStream(AudioManager.STREAM_MUSIC);
		setContentView(R.layout.activity_settings);
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
			btnreset = (Button) findViewById(R.id.reset);
			btnreset.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					deleteWords();
					deleteKnownCategories();

				}
			});
			enFlag = (ImageView) findViewById(R.id.enFlag);
			enFlag.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					setLanguage("en");
				}
			});
			frFlag = (ImageView) findViewById(R.id.frFlag);
			frFlag.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					setLanguage("fr");
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
	 * This method set a particular language as the current language
	 * 
	 * @param v
	 */
	public void setLanguage(String language) {
		languageToLoad = language;
		Log.d(TAG, "the language chosen is: " + language);
		Locale locale = new Locale(languageToLoad);
		Locale.setDefault(locale);
		Configuration config = new Configuration();
		config.locale = locale;
		getResources().updateConfiguration(config,
				getResources().getDisplayMetrics());
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

	public static void setLanguage(){
		languageToLoad = Locale.getDefault().getLanguage();
		if(!(languageToLoad.contains("en") || languageToLoad.contains("fr"))) languageToLoad = "en";
	}
}
