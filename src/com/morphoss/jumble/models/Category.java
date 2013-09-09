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
package com.morphoss.jumble.models;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;

import com.morphoss.jumble.Util;
import com.morphoss.jumble.database.JumbleCategoryTable;
import com.morphoss.jumble.database.JumbleProvider;
import com.morphoss.jumble.frontend.CategoryScreenActivity;
import com.morphoss.jumble.frontend.SettingsActivity;

public class Category {

	/**
	 * This class sets all the details about the a Category type
	 */
	public static String TAG = "Category";

	private int id;
	private HashMap<String, String> names;
	private static final String defaultCC = "en";
	private String imagePath;
	private boolean unlocked = false;
	private ArrayList<Word> words = new ArrayList<Word>();
	private ArrayList<Word> testWords = new ArrayList<Word>();
	private ArrayList<String> solved = new ArrayList<String>();
	private ArrayList<Word> wordsEasy = new ArrayList<Word>();
	private ArrayList<Word> wordsMedium = new ArrayList<Word>();
	private ArrayList<Word> wordsAdvanced = new ArrayList<Word>();

	/**
	 * 
	 * @param context
	 *            required to work
	 * @return the drawable of the category
	 */
	public BitmapDrawable getImage(Context context) {

		return Util.getImage(context, imagePath);
	}

	private Category(int id, JSONObject data) throws JSONException {
		this.id = id;
		;
		this.imagePath = data.getString("image");

		JSONObject translations = data.getJSONObject("translations");
		names = new HashMap<String, String>();
		JSONArray keys = translations.names();
		for (int i = 0; i < keys.length(); i++) {
			String key = (String) keys.get(i);
			String name = translations.getString(key);
			names.put(key, name);
			Log.d(TAG, "mapping key to name: " + key + ":" + name);
		}

		Log.d(TAG, "Created category : " + this);
	}

	/**
	 * This method gets all the categories informations from the JSON file
	 * 
	 * @param json
	 * @return
	 * @throws JSONException
	 */
	public static HashMap<Integer, Category> getCategoriesFromJson(
			JSONObject json) throws JSONException {
		Log.d(TAG, "Creating Category List from json: " + json);
		HashMap<Integer, Category> categories = new HashMap<Integer, Category>();
		JSONArray keys = json.names();
		for (int i = 0; i < keys.length(); i++) {
			String key = (String) keys.get(i);
			JSONObject data = json.getJSONObject(key);
			int id = Integer.parseInt(key);
			categories.put(id, new Category(id, data));

		}
		return categories;

	}

	@Override
	public String toString() {
		String value = "Id: " + id + " ImagePath: " + imagePath
				+ "\nTranslations:\n";
		for (String key : names.keySet())
			value += "\t" + key + ":" + names.get(key) + "\n";
		value += "Words:\n";
		for (Word w : words)
			value += "\t" + w.toString() + "\n";
		return value;
	}

	public void addWord(Word word) {
		words.add(word);
	}

	public int getId() {
		return this.id;
	}

	/**
	 * This method get the localised name of a word if useful if the country
	 * code is not en
	 * 
	 * @param context
	 * @return
	 */
	public String getLocalisedName() {
		String cc = SettingsActivity.getLanguageToLoad();

		Log.d(TAG, "Current CC is " + cc);
		if (names.containsKey(cc)) {
			return names.get(cc);
		}
		return names.get(defaultCC);
	}

	public String getName() {
		return names.get(defaultCC);
	}

	public int size() {
		return words.size();
	}

	/**
	 * 
	 * @param context
	 *            required to work
	 * @return the next word of the available ones
	 */
	public Word getNextWord(Context context) {
	
		solved = CategoryWords.getSolvedWordsFromCategory(context, this);
		printWordList("All Solved Words", solved);
		double ratioSolved = (solved.size() / words.size());

		if (!unlocked() && ratioSolved >= 0.05) {
			ContentValues cv = new ContentValues();
			cv.put(JumbleCategoryTable.UNLOCK, true);
			cv.put(JumbleCategoryTable.CATEGORY, getLocalisedName());
			context.getContentResolver().insert(
					JumbleProvider.CONTENT_URI_CATEGORIES, cv);
			setUnlocked(true);
		}
		Word word = CategoryWords.getRandomItem(testWords);

		return word;
	}

	public void getNewWords(Context context) {
		ArrayList<Word> tempWords = this.words;

		tempWords = CategoryWords.getLocalisedWordsFromCategory(tempWords);
		printWordList("All Localised Available Words", tempWords);
		solved = CategoryWords.getSolvedWordsFromCategory(context, this);
		printWordList("All Solved Words", solved);
		double ratioSolved = (solved.size() / tempWords.size());
		tempWords = CategoryWords.removeSolvedFromList(context, tempWords, solved);
		printWordList("All Available Words", tempWords);

		wordsEasy = CategoryWords.getWordsByDifficulty(tempWords, Difficulty.EASY);
		printWordList("All Easy Available Words", wordsEasy);
		wordsMedium = CategoryWords.getWordsByDifficulty(tempWords,
				Difficulty.MEDIUM);
		printWordList("All Medium Available Words", wordsMedium);
		wordsAdvanced = CategoryWords.getWordsByDifficulty(tempWords,
				Difficulty.ADVANCED);
		printWordList("All Advanced Available Words", wordsAdvanced);

		int countEasyWords = CategoryWords.getCountAllByDifficulty(wordsEasy,
				Difficulty.EASY);
		Log.d(TAG, "count of easy words :" + countEasyWords);
		int countMediumWords = CategoryWords.getCountAllByDifficulty(
				wordsMedium, Difficulty.MEDIUM);
		Log.d(TAG, "count of medium words :" + countMediumWords);
		int countAdvancedWords = CategoryWords.getCountAllByDifficulty(
				wordsAdvanced, Difficulty.ADVANCED);
		Log.d(TAG, "count of advanced words :" + countAdvancedWords);

		int countEasiestWords = CategoryWords.getCountAllButEasiest(tempWords);
		Log.d(TAG, "count of easiest words :" + countEasiestWords);

		ArrayList<Word> filteredwords = new ArrayList<Word>();
		if (countEasyWords > 5) {
			filteredwords = wordsEasy;
		} else if (countMediumWords != 0 && countEasyWords <= 5) {
			filteredwords.addAll(wordsEasy);
			filteredwords.addAll(wordsMedium);
		} else if (countEasyWords == 00 && countMediumWords <= 5) {
			filteredwords.addAll(wordsAdvanced);
		} else {
			filteredwords = tempWords;
			CategoryWords.removeAllButEasiest(filteredwords);
		}
		testWords = filteredwords;
	}

	/**
	 * This method creates a log to show on logcat what are the available words
	 * 
	 * @param message
	 * @param list
	 */
	public void printWordList(String message, ArrayList<?> list) {
		// log message displaying all the elements of list
		Log.d(TAG, message);
		String listString = "";
		for (Object o : list)
			listString += o + ",";
		Log.d(TAG, listString);
	}

	public boolean unlocked() {
		return unlocked;
	}

	public void setUnlocked(boolean unlocked) {
		this.unlocked = unlocked;
	}
}
