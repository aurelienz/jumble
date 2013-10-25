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

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;

import org.json.JSONException;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.morphoss.jumble.BaseActivity;
import com.morphoss.jumble.R;
import com.morphoss.jumble.Util;
import com.morphoss.jumble.database.JumbleCategoryTable;
import com.morphoss.jumble.database.JumbleProvider;
import com.morphoss.jumble.models.Category;
import com.morphoss.jumble.models.ModelParser;

public class ResultsActivity extends BaseActivity {

	/**
	 * This class informs the user of the current score and the scores needed to
	 * unlock new levels
	 */
	private static final String TAG = "ResultsActivity";
	private ResultsGridAdapter rga;
	private int currentCategory = 0;
	private View currentCategoryView = null;
	LinearLayout myGallery;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_result);

		TextView textBestScore = (TextView) findViewById(R.id.score_best);
		textBestScore.setText("" + MainActivity.scoreTotal);
		myGallery = (LinearLayout) findViewById(R.id.galleryCategories);
		myGallery.removeAllViews();
		rga = new ResultsGridAdapter(this);
		for (int i = 0; i < rga.getCount(); i++) {
			myGallery.addView(rga.getView(i, null, myGallery));

		}

		new LoadCategoryTask().execute();
	}

	/**
	 * This method starts the background music
	 */
	protected void onStart() {
		super.onStart();
		myApp.playMusic("generale.ogg");
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
	 * This methods gets back to the MainActivity
	 * 
	 * @param v
	 */
	public void getBack(View v) {
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
		finish();
	}

	/**
	 * This method permits to set the score to the beginning value
	 * 
	 * @param v
	 */
	public void resetScore(View v) {
		MainActivity.scoreTotal = 100;
		setContentView(R.layout.activity_result);
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

	private class LoadCategoryTask extends
			AsyncTask<Void, Void, Collection<Category>> {

		@Override
		protected Collection<Category> doInBackground(Void... params) {

			File file = new File(
					Util.createInternalStorage(ResultsActivity.this)
							+ File.separator + "words.json");

			try {
				String json = Util.getStringFromFile(file);
				Log.d(TAG, "Loaded json: " + json);
				return ModelParser.readJsonData(json);

			} catch (IOException e) {
				Log.e(TAG, "Error reading file: " + e.getMessage());

				return null;
			} catch (JSONException e) {
				Log.e(TAG, "Error reading file: " + e.getMessage());
				return null;
			}

		}

		@Override
		public void onPostExecute(Collection<Category> result) {
			if (result == null) {
				Log.e(TAG, "Could not load categories!!!!!");
				finish();
				return;
			}

			try {
				rga.setCategories(result);
				myGallery.removeAllViews();
				for (int i = 0; i < rga.getCount(); i++) {
					final int num = i;
					View v = rga.getView(i, null, null);
					myGallery.addView(v, i);
					rga.setLayout(v);

				}
			} catch (Exception e) {
				Log.w(TAG, "Exception", e);
			}
		}
	}
}
