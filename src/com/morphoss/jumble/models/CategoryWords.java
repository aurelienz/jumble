package com.morphoss.jumble.models;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.util.Log;

import com.morphoss.jumble.database.JumbleProvider;
import com.morphoss.jumble.database.JumbleWordsTable;
import com.morphoss.jumble.frontend.SettingsActivity;

public class CategoryWords {

	public static String TAG = "CategoryWords";

	public static String currentCategory;
	private ArrayList<Word> words = new ArrayList<Word>();

	public CategoryWords() {
		super();
	}

	public CategoryWords(String currentCategory, ArrayList<Word> words) {
		super();
		CategoryWords.currentCategory = currentCategory;
		this.words = words;
	}

	public static ArrayList<Word> getLocalisedWordsFromCategory(
			ArrayList<Word> list) {
		ArrayList<Word> filteredWords = new ArrayList<Word>();
		for (Word word : list) {
			if (word.hasLocalisation())
				filteredWords.add(word);
		}

		return filteredWords;
	}

	public static ArrayList<Word> getWordsByDifficulty(ArrayList<Word> list,
			Difficulty diff) {
		ArrayList<Word> filteredWords = new ArrayList<Word>();
		for (Word word : list) {
			if (word.getLevel() == diff) {
				filteredWords.add(word);
			}
		}
		return filteredWords;

	}

	public static ArrayList<String> getSolvedWordsFromCategory(Context context,
			Category category) {
		// query contentprovider and return list

		// get the current countrycode
		String cc = SettingsActivity.getLanguageToLoad();

		// show the column of words
		String[] projection = { JumbleWordsTable.WORD };
		// select the words with the current category and current country code
		String selection = JumbleWordsTable.CATEGORY + " = ? AND "
				+ JumbleWordsTable.CC + " = ?";
		String[] selectionArgs = new String[] { category.getLocalisedName(), cc };

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

	public static ArrayList<Word> removeSolvedFromList(Context context,
			ArrayList<Word> wordList, ArrayList<String> solvedList) {

		ArrayList<Word> filteredWords = new ArrayList<Word>();

		for (Word word : wordList) {

			if (!solvedList.contains(word.getNameKey())) {
				filteredWords.add(word);

			}
		}
		return filteredWords;
	}

	public static ArrayList<Word> removeKnownWordsFromList(Context context,
			ArrayList<Word> wordList, ArrayList<String> solvedList) {

		ArrayList<Word> filteredWords = new ArrayList<Word>();

		for (Word word : wordList) {

			if (!solvedList.contains(word.getNameKey())) {
				filteredWords.add(word);
			} else {
				int score = getScorefromTable(context, word.getNameKey());
				if (score < 2000) {
					filteredWords.add(word);
				}

			}
		}
		return filteredWords;
	}

	public static int getScorefromTable(Context context, String word) {
		// get the score of the current word
		String[] projection = { JumbleWordsTable.SCORE };
		// select the columns where WORD = word and CC = currentCC
		String selection = JumbleWordsTable.WORD + " = ? AND "
				+ JumbleWordsTable.CC + " =?";
		String[] selectionArgs = new String[] { word,
				SettingsActivity.getLanguageToLoad() };
		// cursor on the unique row pointing on the score with WORD=word and
		// CC=currentCC
		Cursor cursor = null;
		try {
			cursor = context.getContentResolver().query(
					JumbleProvider.CONTENT_URI_WORDS, projection, selection,
					selectionArgs, null);
			// set the maximum score in a local integer
			if (!cursor.moveToFirst())
				return 0;
			ContentValues myRow = new ContentValues();
			DatabaseUtils.cursorRowToContentValues(cursor, myRow);
			return myRow.getAsInteger(JumbleWordsTable.SCORE);
		} catch (Exception e) {
			Log.e(TAG, Log.getStackTraceString(e));
		} finally {
			if (cursor != null)
				cursor.close();
		}
		return 0;
	}

	public static void removeAllButEasiest(ArrayList<Word> words) {
		// Whats the easiest?
		Difficulty easiest = Difficulty.ADVANCED;

		for (Word word : words) {
			if (word.getLevel().ordinal() < easiest.ordinal()) {
				easiest = word.getLevel();

			}
		}
		Iterator<Word> iterator = words.iterator();
		while (iterator.hasNext()) {
			Word word1 = iterator.next();
			if (word1.getLevel() != easiest) {
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

	public static int getCountAllButEasiest(ArrayList<Word> words) {

		// give the easiest level available
		Difficulty easiest = Difficulty.ADVANCED;
		int count = 0;
		for (Word word : words) {
			if (word.getLevel().ordinal() < easiest.ordinal()) {
				easiest = word.getLevel();
			}
			if (word.getLevel() == easiest)
				count += 1;
		}

		return count;
	}

	public static int getCountAllByDifficulty(ArrayList<Word> words,
			Difficulty diff) {

		int count = 0;
		for (Word word : words) {

			if (word.getLevel() == diff)
				count += 1;
		}

		return count;
	}
}
