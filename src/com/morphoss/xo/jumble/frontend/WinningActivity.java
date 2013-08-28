package com.morphoss.xo.jumble.frontend;

import java.io.File;
import java.io.IOException;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.morphoss.xo.jumble.BaseActivity;
import com.morphoss.xo.jumble.Constants;
import com.morphoss.xo.jumble.R;
import com.morphoss.xo.jumble.database.JumbleProvider;
import com.morphoss.xo.jumble.database.JumbleScoresTable;

public class WinningActivity extends BaseActivity {

	private static final String TAG = "WinningActivity";
	public final static String EXTRA_MESSAGE = "com.morphoss.xo.jumble.MESSAGE";
	public static final int RESULT_PLAY_NEXT = 1;
	public static final int RESULT_RETURN_HOME = 2;
	public static final int RESULT_RETURN_CATEGORY = 3;
	public static final String HAVE_MORE_WORDS = "more words";
	public static final String AVATAR_ID = "avatar id";
	public static ContentResolver resolver;
	public static int scoreWord = 0;
	private PopupWindow pwindowLevel;
	private ImageView btnClosePopupLevel;
	private boolean newLevel = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_winning);
		myApp.stopPlaying();
		Log.d(TAG, "number moves done : " + JumbleActivity.numberMoves);
		setVisibleStars();
		startStarAnimation(R.id.star11);
		startStarAnimation(R.id.star12);
		startStarAnimation(R.id.star13);
		Bundle extras = getIntent().getExtras();
		boolean more_words = extras.getBoolean(HAVE_MORE_WORDS);
		if (!more_words)
			findViewById(R.id.nextWord).setVisibility(View.INVISIBLE);
		findViewById(R.id.nextWord).setVisibility(View.VISIBLE);
		int avatarID = extras.getInt(AVATAR_ID);
		insertScore(scoreWord,
				JumbleActivity.currentCategory.getLocalisedName(this),
				SettingsActivity.getLanguageToLoad());
		setAvatar(avatarID);
		MainActivity.scoreTotal += scoreWord;
		Log.d(TAG, "total score :" + MainActivity.scoreTotal);
		TextView textScore = (TextView) findViewById(R.id.score_value_total);
		textScore.setText("" + MainActivity.scoreTotal);

		Button playImage = (Button) findViewById(R.id.nextWord);
		AnimationDrawable playAnimation = (AnimationDrawable) playImage
				.getBackground();
		playAnimation.start();

	}

	private void setVisibleStars() {
		if (JumbleActivity.numberMoves == JumbleActivity.correctWord
				.getLocalisedWord(this).length() && JumbleActivity.time < 15000) {
			// 3 stars
			findViewById(R.id.star11).setVisibility(View.VISIBLE);
			findViewById(R.id.star12).setVisibility(View.VISIBLE);
			findViewById(R.id.star13).setVisibility(View.VISIBLE);
			scoreWord = 100;
		} else if (JumbleActivity.numberMoves == JumbleActivity.correctWord
				.getLocalisedWord(this).length() || JumbleActivity.time < 30000) {
			// 2 stars
			findViewById(R.id.star11).setVisibility(View.GONE);
			findViewById(R.id.star12).setVisibility(View.VISIBLE);
			findViewById(R.id.star13).setVisibility(View.VISIBLE);
			TextView text1 = (TextView) findViewById(R.id.text_win);
			text1.setText(R.string.win2);
			scoreWord = 50;
		} else {
			// 1 star
			findViewById(R.id.star12).setVisibility(View.GONE);
			findViewById(R.id.star11).setVisibility(View.GONE);
			findViewById(R.id.star13).setVisibility(View.VISIBLE);
			TextView text2 = (TextView) findViewById(R.id.text_win);
			text2.setText(R.string.win3);
			scoreWord = 20;
		}

	}

	public void playWord(View view) {
		String sound = JumbleActivity.wordHint.getLocalisedSound(this);
		if (new File(Constants.storagePath + File.separator
				+ JumbleActivity.wordHint.getSoundPath()).exists()) {
			myApp.playSoundJumble(Constants.storagePath + File.separator
					+ sound);
		} else {
			Log.d(TAG, "sound file not found for the word: "
					+ Constants.storagePath + File.separator
					+ JumbleActivity.wordHint.getSoundPath());
		}

	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		Intent intent = new Intent(this, CategoryScreenActivity.class);
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
		JumbleActivity.numberMoves = 0;
		myApp.resumeMusic();
		for (int i = 0; i < CategoryScreenActivity.getPga().getCount(); i++) {
			if (MainActivity.scoreTotal == CategoryGridAdapter.getCategory(i)
					.getMinScore()) {
				newLevel = true;
			}
		}
		final Handler handler = new Handler();
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				// Show the popup window of an unlocked level after 0.5s = 500ms
				if (newLevel) {
					PopupWindowLevel();
				}

			}
		}, 500);
	}

	public void PopupWindowLevel() {

		try {

			LayoutInflater inflater = (LayoutInflater) WinningActivity.this
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View layout = inflater.inflate(R.layout.popup_level,
					(ViewGroup) findViewById(R.id.popup_element));
			pwindowLevel = new PopupWindow(layout, 420, 300, true);
			pwindowLevel.showAtLocation(layout, Gravity.CENTER, 0, 0);
			btnClosePopupLevel = (ImageView) layout
					.findViewById(R.id.btn_close_popup);
			btnClosePopupLevel.setOnClickListener(cancel_popup_level);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private OnClickListener cancel_popup_level = new OnClickListener() {
		public void onClick(View v) {
			newLevel = false;
			pwindowLevel.dismiss();

		}
	};

	@Override
	protected void onStart() {
		super.onStart();
		myApp.playMusic("winning.ogg");
	}

	public void Screen_Home(View view) {
		setResult(RESULT_RETURN_HOME);
		myApp.stopPlaying();
		finish();
	}

	public void Screen_Category(View view) {
		setResult(RESULT_RETURN_CATEGORY);
		myApp.stopPlaying();
		finish();
	}

	public void Screen_Next(View view) throws IOException {

		setResult(RESULT_PLAY_NEXT);
		myApp.stopPlaying();
		myApp.playMusic("jumble.ogg");
		finish();

	}

	private void startStarAnimation(int starId) {
		ImageView starImage = (ImageView) findViewById(starId);
		AnimationDrawable starAnimation = (AnimationDrawable) starImage
				.getDrawable();
		starAnimation.start();
	}

	public void setAvatar(int avatarID) {
		ImageView imageView = (ImageView) findViewById(R.id.avatar_star);
		imageView.setImageDrawable(getResources().getDrawable(avatarID));
	}

	private void insertScore(int score, String category, String cc) {

		ContentValues cv = new ContentValues();
		cv.put(JumbleScoresTable.SCORE, score);
		cv.put(JumbleScoresTable.CATEGORY, category);
		cv.put(JumbleScoresTable.CC, cc);

		resolver = this.getContentResolver();
		resolver.insert(JumbleProvider.CONTENT_URI_SCORES, cv);

		Log.d(TAG, "add the score:" + score + " from category : " + category
				+ " with cc :" + cc + " in database");
	}

}