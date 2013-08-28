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
import java.util.Collection;

import org.json.JSONException;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.NavUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;

import com.morphoss.xo.jumble.BaseActivity;
import com.morphoss.xo.jumble.Constants;
import com.morphoss.xo.jumble.R;
import com.morphoss.xo.jumble.Util;
import com.morphoss.xo.jumble.models.Category;
import com.morphoss.xo.jumble.models.ModelParser;

public class CategoryScreenActivity extends BaseActivity {

	/**
	 * This class displays the categories and load them from the JSON file
	 */
	private static final String TAG = "CategoryActivity";
	private static final int CATEGORIES_LOADED = 0;
	private static final int CATEGORIES_NOT_LOADED = 1;
	private GridLayout gridLayout;
	private static CategoryGridAdapter pga;

	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {

			// The list of categories has been created - populate the Adapter
			// with views.
			if (msg.what == CATEGORIES_LOADED) {
			} else if (msg.what == CATEGORIES_NOT_LOADED) {
				finish();
			}
		}
	};

	/**
	 * 
	 * @return the category gridadapter
	 */
	public static CategoryGridAdapter getPga() {
		return pga;
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

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_category);
		gridLayout = (GridLayout) findViewById(R.id.gridView1);
		gridLayout.setRowCount(2);
		DisplayMetrics displaymetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
		int height = displaymetrics.heightPixels;
		int width = displaymetrics.widthPixels;
		pga = new CategoryGridAdapter(this, width, height);
		new LoadCategoryTask().execute();

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

			File file = new File(Constants.storagePath + File.separator
					+ "words.json");

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
				mHandler.sendEmptyMessage(CATEGORIES_NOT_LOADED);

			} else {
				pga.setCategories(result);
				mHandler.sendEmptyMessage(CATEGORIES_LOADED);
				gridLayout.removeAllViews();
				for (int i = 0; i < pga.getCount(); i++) {
					final int num = i;
					View v = pga.getView(i, null, gridLayout);
					gridLayout.addView(v, i);
					if (MainActivity.scoreTotal >= CategoryGridAdapter
							.getCategory(i).getMinScore()) {
						if (MainActivity.scoreTotal == CategoryGridAdapter
								.getCategory(i).getMinScore()) {
							Log.d(TAG, "a category is unlocked");
						}
						v.setOnClickListener(new OnClickListener() {
							public void onClick(View v) {
								Intent intent = new Intent(
										CategoryScreenActivity.this,
										JumbleActivity.class);
								intent.putExtra(JumbleActivity.CATEGORY_KEY,
										num);
								intent.putExtra(WinningActivity.AVATAR_ID,
										AvatarActivity.id);
								startActivity(intent);
								finish();
							}
						});
					}

				}
			}
		}

	}

}
