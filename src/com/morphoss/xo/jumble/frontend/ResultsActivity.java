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

	private static final String TAG = "ResultsActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_result);

		TextView textBestScore = (TextView) findViewById(R.id.score_best);
		textBestScore.setText("" + MainActivity.scoreTotal);

	}

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

	public void getBack(View v) {
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
		finish();
	}

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
