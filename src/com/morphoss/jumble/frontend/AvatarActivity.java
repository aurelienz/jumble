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

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.morphoss.jumble.BaseActivity;
import com.morphoss.jumble.R;

public class AvatarActivity extends BaseActivity {

	/**
	 * This class sets the choice of the avatar
	 */
	private static final String TAG = "AvatarActivity";
	private AvatarGridAdapter aga;
	public static int id = 0;
	LinearLayout myGallery;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_avatar);
		myGallery = (LinearLayout) findViewById(R.id.mygallery);
		myGallery.removeAllViews();
		aga = new AvatarGridAdapter(this);
		for (int i = 0; i < aga.getCount(); i++) {
			myGallery.addView(aga.getView(i, null, myGallery));

		}
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

	/**
	 * This method starts the CategoryScreenActivity
	 * 
	 * @param view
	 */
	public void Screen_Category(View view) {
		Intent intent = new Intent(this, CategoryScreenActivity.class);
		id = aga.getCurrentAvatar();
		intent.putExtra(WinningActivity.AVATAR_ID, AvatarActivity.id);
		startActivity(intent);
		finish();
	}

}
