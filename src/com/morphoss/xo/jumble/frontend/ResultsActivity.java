/*
 * Copyright (C) 2011 Morphoss Ltd
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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;

import org.json.JSONException;

import android.content.Intent;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

import com.morphoss.xo.jumble.BaseActivity;
import com.morphoss.xo.jumble.Constants;
import com.morphoss.xo.jumble.R;
import com.morphoss.xo.jumble.Util;
import com.morphoss.xo.jumble.models.Category;
import com.morphoss.xo.jumble.models.ModelParser;

public class ResultsActivity extends BaseActivity {

	/**
	 * This class informs the user of the current score and the scores needed to
	 * unlock new levels
	 */
	private static final String TAG = "ResultsActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_result);

		TextView textBestScore = (TextView) findViewById(R.id.score_best);
		textBestScore.setText("" + MainActivity.scoreTotal);

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

}
