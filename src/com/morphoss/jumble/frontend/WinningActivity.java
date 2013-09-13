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
import java.util.ArrayList;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.morphoss.jumble.BaseActivity;
import com.morphoss.jumble.R;
import com.morphoss.jumble.Util;
import com.morphoss.jumble.database.JumbleProvider;
import com.morphoss.jumble.database.JumbleCategoryTable;
import com.morphoss.jumble.database.JumbleWordsTable;
import com.morphoss.jumble.models.Category;
import com.morphoss.jumble.models.CategoryWords;
import com.morphoss.jumble.models.Word;

public class WinningActivity extends BaseActivity {

	/**
	 * This class displays the WinningActivity
	 */
	private static final String TAG = "WinningActivity";
	public final static String EXTRA_MESSAGE = "com.morphoss.xo.jumble.MESSAGE";
	public static final int RESULT_PLAY_NEXT = 1;
	public static final int RESULT_RETURN_HOME = 2;
	public static final int RESULT_RETURN_CATEGORY = 3;
	public static final String HAVE_MORE_WORDS = "more words";
	public static final String AVATAR_ID = "avatar id";
	public static ContentResolver resolver;
	public static int scoreWord = 0;

	public static int scoreDatabase = 100;

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
		setAvatar(avatarID);
		MainActivity.scoreTotal += scoreWord;
		Log.d(TAG, "total score :" + MainActivity.scoreTotal);
		TextView textScore = (TextView) findViewById(R.id.score_value_total);
		textScore.setText("" + MainActivity.scoreTotal);
		insertWord(WinningActivity.this,
				JumbleActivity.correctWord.getNameKey(),
				JumbleActivity.currentCategory.getLocalisedName(),
				SettingsActivity.getLanguageToLoad(), scoreDatabase);

		Button playImage = (Button) findViewById(R.id.nextWord);
		AnimationDrawable playAnimation = (AnimationDrawable) playImage
				.getBackground();
		playAnimation.start();

	}

	/**
	 * This method sets the number of starts according to the time and the
	 * number of moves done
	 */
	private void setVisibleStars() {
		if (JumbleActivity.numberMoves == JumbleActivity.correctWord
				.getLocalisedWord().length() && JumbleActivity.time < 15000) {
			// 3 stars
			findViewById(R.id.star11).setVisibility(View.VISIBLE);
			findViewById(R.id.star12).setVisibility(View.VISIBLE);
			findViewById(R.id.star13).setVisibility(View.VISIBLE);
			scoreWord = 100;
			scoreDatabase = 1000;
		} else if (JumbleActivity.numberMoves == JumbleActivity.correctWord
				.getLocalisedWord().length() || JumbleActivity.time < 30000) {
			// 2 stars
			findViewById(R.id.star11).setVisibility(View.GONE);
			findViewById(R.id.star12).setVisibility(View.VISIBLE);
			findViewById(R.id.star13).setVisibility(View.VISIBLE);
			TextView text1 = (TextView) findViewById(R.id.text_win);
			text1.setText(R.string.win2);
			scoreWord = 50;
			scoreDatabase = 50;
		} else {
			// 1 star
			findViewById(R.id.star12).setVisibility(View.GONE);
			findViewById(R.id.star11).setVisibility(View.GONE);
			findViewById(R.id.star13).setVisibility(View.VISIBLE);
			TextView text2 = (TextView) findViewById(R.id.text_win);
			text2.setText(R.string.win3);
			scoreWord = 20;
			scoreDatabase = 0;
		}

	}

	/**
	 * This method plays the pronunciation of the word
	 * 
	 * @param view
	 */
	public void playWord(View view) {
		String sound = JumbleActivity.wordHint.getSoundPath();
		if (sound != null) {
			myApp.playSoundJumble(Util.createInternalStorage(this)
					+ File.separator + sound);
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

	}


	@Override
	protected void onStart() {
		super.onStart();
		myApp.playMusic("winning.ogg");
	}

	/**
	 * This method goes back to the MainActivity
	 * 
	 * @param view
	 */
	public void Screen_Home(View view) {
		setResult(RESULT_RETURN_HOME);
		myApp.stopPlaying();
		finish();
	}

	/**
	 * This method goes back to the CategoryScreenActivity
	 * 
	 * @param view
	 */
	public void Screen_Category(View view) {
		setResult(RESULT_RETURN_CATEGORY);
		myApp.stopPlaying();
		finish();
	}

	/**
	 * This method starts a new word
	 * 
	 * @param view
	 * @throws IOException
	 */
	public void Screen_Next(View view) throws IOException {

		setResult(RESULT_PLAY_NEXT);
		myApp.stopPlaying();
		myApp.playMusic("jumble.ogg");
		finish();

	}

	/**
	 * This methods starts animations on the stars
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
	 * This method gets the ID of the avatar selected and displays its picture
	 * on the screen
	 * 
	 * @param avatarID
	 */
	public void setAvatar(int avatarID) {
		ImageView imageView = (ImageView) findViewById(R.id.avatar_star);
		imageView.setImageDrawable(getResources().getDrawable(avatarID));
	}

	
	private void insertWord(Context context, String word, String category,
			String cc, int scoreOfWord) {

		if ( scoreOfWord == 0 ) return;

		resolver = this.getContentResolver();
		ContentValues cv = new ContentValues();
		ArrayList<String> solved = CategoryWords.getSolvedWordsFromCategory(context, JumbleActivity.currentCategory);
		String action = null;
		if (solved.contains(word)) {
			action = "update";
			// don't add a new row, just update the score
			cv.put(JumbleWordsTable.ADDSCORE, scoreOfWord);
			resolver.update(Uri.withAppendedPath(JumbleProvider.CONTENT_URI_WORDS, "addscore"), cv, 
					JumbleWordsTable.WORD+"=? AND "+JumbleWordsTable.CC+"=?",
					new String[] { word, cc } );
			
		} else {
			action = "insert";
			cv.put(JumbleWordsTable.WORD, word);
			cv.put(JumbleWordsTable.CATEGORY, category);
			cv.put(JumbleWordsTable.CC, cc);
			cv.put(JumbleWordsTable.SCORE, scoreOfWord);
			resolver.insert(JumbleProvider.CONTENT_URI_WORDS, cv);
		}

		Log.d(TAG, action+" the word:" + word + " from category : " + category
				+ " with cc :" + cc + " with score :" + scoreOfWord
				+ ".  Database score is now " + CategoryWords.getScorefromTable(context, word));
	}

}
