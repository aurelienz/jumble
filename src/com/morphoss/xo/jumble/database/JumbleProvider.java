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

package com.morphoss.xo.jumble.database;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

public class JumbleProvider extends ContentProvider {
	/**
	 * This method creates two tables: words and scores
	 */
	public static final String AUTHORITY = "com.morphoss.xo.jumble";
	public static final Uri CONTENT_URI_WORDS = Uri.parse("content://"
			+ AUTHORITY + "/words");
	public static final Uri CONTENT_URI_SCORES = Uri.parse("content://"
			+ AUTHORITY + "/scores");
	public static final String TAG = "JumbleProvider";

	private static final int ALL_WORDS = 1;
	private static final int SINGLE_WORD = 2;
	private static final int ALL_SCORES = 3;
	private static final int SINGLE_SCORE = 4;

	private static final UriMatcher uriMatcher;
	static {
		uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		uriMatcher.addURI(AUTHORITY, "words", ALL_WORDS);
		uriMatcher.addURI(AUTHORITY, "scores", ALL_SCORES);
		uriMatcher.addURI(AUTHORITY, "words/#", SINGLE_WORD);
		uriMatcher.addURI(AUTHORITY, "scores/#", SINGLE_SCORE);
	}

	private JumbleDatabaseHelper dbHelper;

	@Override
	public boolean onCreate() {
		this.dbHelper = new JumbleDatabaseHelper(getContext());
		return false;
	}

	@Override
	public String getType(Uri uri) {
		switch (uriMatcher.match(uri)) {
		case ALL_WORDS:
			return "vnd.android.cursor.dir/vnd.com.morphoss.xo.jumble.words";
		case ALL_SCORES:
			return "vnd.android.cursor.dir/vnd.com.morphoss.xo.jumble.scores";
		case SINGLE_WORD:
			return "vnd.android.cursor.item/vnd.com.morphoss.xo.jumble.words";
		case SINGLE_SCORE:
			return "vnd.android.cursor.item/vnd.com.morphoss.xo.jumble.scores";
		default:
			throw new IllegalArgumentException("Unsupported URI: " + uri);
		}
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		Log.d(TAG, "Inserting row: " + values);
		SQLiteDatabase db = dbHelper.getWritableDatabase();

		switch (uriMatcher.match(uri)) {
		case ALL_WORDS:
			long id = db.insert(JumbleWordsTable.TABLE, null, values);
			getContext().getContentResolver().notifyChange(uri, null);
			return Uri.parse(CONTENT_URI_WORDS + "/" + id);

		case ALL_SCORES:
			long id2 = db.insert(JumbleScoresTable.TABLE, null, values);
			getContext().getContentResolver().notifyChange(uri, null);
			return Uri.parse(CONTENT_URI_SCORES + "/" + id2);

		default:

			throw new IllegalArgumentException("Unsupported URI: " + uri);
		}
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

		switch (uriMatcher.match(uri)) {

		case SINGLE_WORD:
			queryBuilder.setTables(JumbleWordsTable.TABLE);
			String id = uri.getPathSegments().get(1);
			queryBuilder.appendWhere(JumbleWordsTable._ID + "=" + id);
			Cursor cursor1 = queryBuilder.query(db, projection, selection,
					selectionArgs, null, null, sortOrder);
			return cursor1;

		case SINGLE_SCORE:
			queryBuilder.setTables(JumbleScoresTable.TABLE);
			String id2 = uri.getPathSegments().get(1);
			queryBuilder.appendWhere(JumbleScoresTable._ID + "=" + id2);
			Cursor cursor2 = queryBuilder.query(db, projection, selection,
					selectionArgs, null, null, sortOrder);
			return cursor2;

		case ALL_WORDS:
			queryBuilder.setTables(JumbleWordsTable.TABLE);
			Cursor cursor3 = queryBuilder.query(db, projection, selection,
					selectionArgs, null, null, sortOrder);
			return cursor3;

		case ALL_SCORES:
			queryBuilder.setTables(JumbleScoresTable.TABLE);
			Cursor cursor4 = queryBuilder.query(db, projection, selection,
					selectionArgs, null, null, sortOrder);
			return cursor4;

		default:
			throw new IllegalArgumentException("Unsupported URI: " + uri);
		}
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		int deleteCount = db.delete(JumbleWordsTable.TABLE, selection,
				selectionArgs);
		int deleteCount2 = db.delete(JumbleScoresTable.TABLE, selection,
				selectionArgs);
		switch (uriMatcher.match(uri)) {

		case SINGLE_WORD:
			String id = uri.getPathSegments().get(1);
			selection = JumbleWordsTable._ID
					+ "="
					+ id
					+ (!TextUtils.isEmpty(selection) ? " AND (" + selection
							+ ')' : "");
			getContext().getContentResolver().notifyChange(uri, null);
			return deleteCount;
		case ALL_WORDS:
			Log.d(TAG, "delete all the words of the database");
			getContext().getContentResolver().notifyChange(uri, null);
			return deleteCount;
		case SINGLE_SCORE:
			String id2 = uri.getPathSegments().get(1);
			selection = JumbleScoresTable._ID
					+ "="
					+ id2
					+ (!TextUtils.isEmpty(selection) ? " AND (" + selection
							+ ')' : "");
			getContext().getContentResolver().notifyChange(uri, null);
			return deleteCount2;
		case ALL_SCORES:
			Log.d(TAG, "delete all the scores of the database");
			getContext().getContentResolver().notifyChange(uri, null);
			return deleteCount2;

		default:
			throw new IllegalArgumentException("Unsupported URI: " + uri);
		}

	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		switch (uriMatcher.match(uri)) {

		case SINGLE_WORD:
			String id = uri.getPathSegments().get(1);
			selection = JumbleWordsTable._ID
					+ "="
					+ id
					+ (!TextUtils.isEmpty(selection) ? " AND (" + selection
							+ ')' : "");
			int updateCount = db.update(JumbleWordsTable.TABLE, values,
					selection, selectionArgs);
			getContext().getContentResolver().notifyChange(uri, null);
			return updateCount;
		case SINGLE_SCORE:
			String id2 = uri.getPathSegments().get(1);
			selection = JumbleScoresTable._ID
					+ "="
					+ id2
					+ (!TextUtils.isEmpty(selection) ? " AND (" + selection
							+ ')' : "");
			int updateCount2 = db.update(JumbleScoresTable.TABLE, values,
					selection, selectionArgs);
			getContext().getContentResolver().notifyChange(uri, null);
			return updateCount2;
		default:
			throw new IllegalArgumentException("Unsupported URI: " + uri);
		}

	}

}
