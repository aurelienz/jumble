package com.morphoss.xo.jumble.frontend;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.morphoss.xo.jumble.BaseActivity;
import com.morphoss.xo.jumble.R;

public class AvatarActivity extends BaseActivity {

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

	public void Screen_Category(View view) {
		Intent intent = new Intent(this, CategoryScreenActivity.class);
		id = aga.getCurrentAvatar();
		intent.putExtra(WinningActivity.AVATAR_ID, AvatarActivity.id);
		startActivity(intent);
		finish();
	}

}
