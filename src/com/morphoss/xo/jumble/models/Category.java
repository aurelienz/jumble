package com.morphoss.xo.jumble.models;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.morphoss.xo.jumble.R;
import com.morphoss.xo.jumble.Util;
import com.morphoss.xo.jumble.database.JumbleProvider;
import com.morphoss.xo.jumble.database.JumbleWordsTable;
import com.morphoss.xo.jumble.frontend.SettingsActivity;

public class Category {

	public static String TAG = "Category";

	private int id;
	private HashMap<String, String> names;
	private static final String defaultCC = "en";
	private ArrayList<Word> words = new ArrayList<Word>();
	private String imagePath;
	private final int minScore;

	public int getMinScore() {
		return minScore;
	}

	public BitmapDrawable getImage(Context context) {

		return Util.getImage(context, imagePath);
	}

	private void populateCache(Context context) {
		InputStream fis = null;
	}

	private Category(int id, JSONObject data) throws JSONException {
		this.id = id;
		;
		this.imagePath = data.getString("image");
		this.minScore = data.getInt("minscore");

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
		String value = "Id: " + id + " ImagePath: " + imagePath + " minScore :"
				+ minScore + "\nTranslations:\n";
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

	public String getLocalisedName(Context context) {
		String cc = SettingsActivity.getLanguageToLoad();

		Log.d(TAG, "Current CC is " + cc);
		if (names.containsKey(cc)) {
			return names.get(cc);
		}
		return names.get(defaultCC);
	}

	public String getName(Context context) {
		return names.get(defaultCC);
	}

	public int size() {
		return words.size();
	}

	public Word getNextWord(Context context) {
		ArrayList<Word> words = this.words; // getAllWordsForThisCategory();
		printWordList("All Available Words", words);
		ArrayList<String> solved = getSolvedWordsForCategory(context, this);
		printWordList("All solved Words", solved);
		words = removeSolvedFromList(words, solved);
		printWordList("All Available Words minus solved", words);

		removeAllButEasiest(context, words);
		printWordList("All but easiest", words);
		Word word = getRandomItem(words);
		return word;

	}

	public static ArrayList<String> getSolvedWordsForCategory(Context context,
			Category category) {
		// query contentprovider and return list

		// get the current countrycode
		String cc = SettingsActivity.getLanguageToLoad();

		// show the column of words
		String[] projection = { JumbleWordsTable.WORD };
		// select the words with the current category and current country code
		String selection = JumbleWordsTable.CATEGORY + " = ? AND "
				+ JumbleWordsTable.CC + " = ?";
		String[] selectionArgs = new String[] { category.getName(context), cc };

		Cursor cursor = context.getContentResolver().query(
				JumbleProvider.CONTENT_URI_WORDS, projection, selection,
				selectionArgs, null);
		ArrayList<String> solvedWords = new ArrayList<String>();
		for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
			ContentValues myRow = new ContentValues();
			DatabaseUtils.cursorRowToContentValues(cursor, myRow);
			Log.d(TAG, "Got row: " + myRow.getAsString(JumbleWordsTable.WORD));
			solvedWords.add(myRow.getAsString(JumbleWordsTable.WORD));

		}
		cursor.close();

		return solvedWords;
	}

	public static ArrayList<Word> removeSolvedFromList(
			ArrayList<Word> wordList, ArrayList<String> solvedList) {

		ArrayList<Word> filteredWords = new ArrayList<Word>();

		for (Word word : wordList) {
			if (!solvedList.contains(word.getNameKey()))
				filteredWords.add(word);
		}

		return filteredWords;
	}

	public static void removeAllButEasiest(Context context,
			ArrayList<Word> words) {
		// Whats the easiest?
		Difficulty easiest = Difficulty.ADVANCED;

		for (Word word : words) {
			if (word.getLocalisedLevel(context).ordinal() < easiest.ordinal()) {
				easiest = word.getLocalisedLevel(context);

			}
		}
		Iterator<Word> iterator = words.iterator();
		while (iterator.hasNext()) {
			Word word1 = iterator.next();
			if (word1.getLocalisedLevel(context) != easiest) {
				iterator.remove();
			}
		}

	}

	public static Word getRandomItem(ArrayList<Word> words) {

		if (words.size() < 1)
			return null;
		Random myRandom = new Random();
		int index = myRandom.nextInt(words.size());
		Word randomWord = words.get(index);
		words.remove(index);

		return randomWord;
	}

	public static void printWordList(String message, ArrayList<?> list) {
		// log message displaying all the elements of list
		Log.d(TAG, message);
		String listString = "";
		for (Object o : list)
			listString += o + ",";
		Log.d(TAG, listString);
	}

}
