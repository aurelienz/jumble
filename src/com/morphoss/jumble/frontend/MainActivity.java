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

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.morphoss.jumble.BaseActivity;
import com.morphoss.jumble.R;
import com.morphoss.jumble.Util;
import com.morphoss.jumble.videos.RulesVideoActivity;
import com.morphoss.jumble.videos.StoryActivity;

public class MainActivity extends BaseActivity {

	/**
	 * This class is the main screen where there are links with most of the
	 * others activities
	 */
	private static final String TAG = "MainActivity";
	public final static String EXTRA_MESSAGE = "com.morphoss.xo.jumble.MESSAGE";
	public static int scoreTotal = 100;
	protected ProgressBar mProgressBar;
	protected static final int TIMER_RUNTIME = 10000;

	private static final String IS_INSTALLED_KEY = "installed";
	private static final String PREFS_FILE = "xo-jumble-prefs";

	protected boolean mbActive;
	private ProgressDialog progressDialog;
	private Button button;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG, "onCreate Called!");
		setContentView(R.layout.activity_main);
		startStarAnimation(R.id.star1);
		startStarAnimation(R.id.star2);
		startStarAnimation(R.id.star3);
		startStarAnimation(R.id.star4);
		startStarAnimation(R.id.star5);
		Button playImage = (Button) findViewById(R.id.button_play);
		AnimationDrawable playAnimation = (AnimationDrawable) playImage
				.getBackground();
		playAnimation.start();

		SettingsActivity.setLanguage();
		Log.d(TAG, "language to load :" + SettingsActivity.getLanguageToLoad());
		// Restore preferences
		SharedPreferences settings = getSharedPreferences(PREFS_FILE,
				Context.MODE_PRIVATE);
		boolean installed = settings.getBoolean(IS_INSTALLED_KEY, false);

		if (installed == false) {

			progressDialog = ProgressDialog.show(MainActivity.this, "",
					getString(R.string.loading));
			new InstallDataTask().execute();
		}
		button = (Button) findViewById(R.id.credits);

		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(MainActivity.this,
						WebViewActivity.class);
				startActivity(intent);
			}

		});
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		Screen_Quit(null);
	}

	@Override
	protected void onPause() {
		super.onPause();
		myApp.pauseMusic();
	}

	@Override
	protected void onResume() {
		super.onResume();
		myApp.resumeMusic();
	}

	@Override
	protected void onStart() {
		super.onStart();
		myApp.playMusic("generale.ogg");
	}

	/**
	 * This method plays animations with stars on the screen
	 * 
	 * @param starId
	 */
	private void startStarAnimation(int starId) {
		ImageView starImage = (ImageView) findViewById(starId);
		AnimationDrawable starAnimation = (AnimationDrawable) starImage
				.getDrawable();
		starAnimation.start();
	}

	/**
	 * This method starts the StoryActivity
	 * 
	 * @param view
	 */
	public void Screen_Play(View view) {
		Intent intent = new Intent(this, StoryActivity.class);
		startActivity(intent);
		finish();

	}

	/**
	 * This method starts the ResultsActivity
	 * 
	 * @param view
	 */
	public void Screen_Results(View view) {
		Intent intent = new Intent(this, ResultsActivity.class);
		startActivity(intent);
		finish();

	}

	/**
	 * This method starts the RulesActivity
	 * 
	 * @param view
	 */
	public void Screen_Rules(View view) {
		Intent intent = new Intent(this, RulesVideoActivity.class);
		startActivity(intent);
		finish();
	}

	/**
	 * This methods starts the SettingsActivity
	 * 
	 * @param view
	 */
	public void Screen_Settings(View view) {
		Intent intent = new Intent(this, SettingsActivity.class);
		startActivity(intent);
		finish();
	}

	/**
	 * This method quits the application
	 * 
	 * @param view
	 */
	public void Screen_Quit(View view) {
		finish();
		myApp.stopPlaying();
	}

	/**
	 * This class load the zip file as a background task
	 * 
	 */
	private class InstallDataTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... arg0) {
			File file = new File(Util.createInternalStorage(MainActivity.this));

			if (!file.exists()) {
				if (!file.mkdir()) {
					Log.e(TAG, "Unable to create file on device - give up!");
					finish();
					return null;
				}
			}

			try {
				InputStream in = getAssets().open("database.zip");
				ZipInputStream zis = new ZipInputStream(
						new BufferedInputStream(in));
				unpackZip(zis, file);
				SharedPreferences settings = getSharedPreferences(PREFS_FILE,
						Context.MODE_PRIVATE);
				settings.edit().putBoolean(IS_INSTALLED_KEY, true).commit();

			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				progressDialog.dismiss();
			}
			return null;
		}

		private void createDirIfNeeded(File destDirectory, ZipEntry entry) {
			String name = entry.getName();

			if (name.contains("/")) {
				int index = name.lastIndexOf("/");
				String dirSequence = name.substring(0, index);
				File newDirs = new File(destDirectory.getAbsolutePath()
						+ File.separator + dirSequence);

				newDirs.mkdirs();
			}
		}

		public boolean unpackZip(ZipInputStream zis, File targetDir)
				throws IOException, FileNotFoundException {
			final int BUFFER_SIZE = 2048;

			BufferedOutputStream dest = null;
			ZipEntry entry = null;

			while ((entry = zis.getNextEntry()) != null) {
				String outputFilename = targetDir.getAbsolutePath()
						+ File.separator + entry.getName();
				createDirIfNeeded(targetDir, entry);
				if (entry.isDirectory())
					continue;
				int count;
				byte data[] = new byte[BUFFER_SIZE];

				// write the file to the disk
				FileOutputStream fos = new FileOutputStream(outputFilename);
				dest = new BufferedOutputStream(fos, BUFFER_SIZE);

				while ((count = zis.read(data, 0, BUFFER_SIZE)) != -1) {
					dest.write(data, 0, count);
				}

				// close the output streams
				dest.flush();
				dest.close();
			}

			// close the zip file
			zis.close();
			return true;
		}
	}
}
