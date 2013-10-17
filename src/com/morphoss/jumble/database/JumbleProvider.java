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

package com.morphoss.jumble.database;

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
	public static final String AUTHORITY = "com.morphoss.jumble";
	public static final Uri CONTENT_URI_WORDS = Uri.parse("content://"
			+ AUTHORITY + "/words");
	public static final Uri CONTENT_URI_CATEGORIES = Uri.parse("content://"
			+ AUTHORITY + "/categories");
	public static final String TAG = "JumbleProvider";

	private static final int ALL_WORDS = 1;
	private static final int SINGLE_WORD = 2;
	private static final int ALL_CATEGORIES = 3;
	private static final int SINGLE_CATEGORY = 4;
	private static final int ADD_SCORE = 5;
	private static final int ADD_RATIO = 6;
	private static final int UNLOCK_CATEGORY = 7;

	private static final UriMatcher uriMatcher;
	static {
		uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		uriMatcher.addURI(AUTHORITY, "words", ALL_WORDS);
		uriMatcher.addURI(AUTHORITY, "categories", ALL_CATEGORIES);
		uriMatcher.addURI(AUTHORITY, "words/#", SINGLE_WORD);
		uriMatcher.addURI(AUTHORITY, "categories/#", SINGLE_CATEGORY);
		uriMatcher.addURI(AUTHORITY, "words/addscore", ADD_SCORE);
		uriMatcher.addURI(AUTHORITY, "categories/addratio", ADD_RATIO);
		uriMatcher.addURI(AUTHORITY, "categories/unlockCategory", UNLOCK_CATEGORY);
		
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
		case ALL_CATEGORIES:
			return "vnd.android.cursor.dir/vnd.com.morphoss.xo.jumble.categories";
		case SINGLE_WORD:
			return "vnd.android.cursor.item/vnd.com.morphoss.xo.jumble.words";
		case SINGLE_CATEGORY:
			return "vnd.android.cursor.item/vnd.com.morphoss.xo.jumble.categories";
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

		case ALL_CATEGORIES:
			long id2 = db.insert(JumbleCategoryTable.TABLE, null, values);
			getContext().getContentResolver().notifyChange(uri, null);
			return Uri.parse(CONTENT_URI_CATEGORIES + "/" + id2);

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

		case SINGLE_CATEGORY:
			queryBuilder.setTables(JumbleCategoryTable.TABLE);
			String id2 = uri.getPathSegments().get(1);
			queryBuilder.appendWhere(JumbleCategoryTable._ID + "=" + id2);
			Cursor cursor2 = queryBuilder.query(db, projection, selection,
					selectionArgs, null, null, sortOrder);
			return cursor2;

		case ALL_WORDS:
			queryBuilder.setTables(JumbleWordsTable.TABLE);
			Cursor cursor3 = queryBuilder.query(db, projection, selection,
					selectionArgs, null, null, sortOrder);
			return cursor3;

		case ALL_CATEGORIES:
			queryBuilder.setTables(JumbleCategoryTable.TABLE);
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
		int deleteCount2 = db.delete(JumbleCategoryTable.TABLE, selection,
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
			Log.d(TAG, "delete all the wordsfrom the database");
			getContext().getContentResolver().notifyChange(uri, null);
			return deleteCount;

		case ALL_CATEGORIES:
			Log.d(TAG, "delete all the unlocked categories from the database");
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
		case ADD_SCORE:
			db.execSQL(
					"UPDATE " + JumbleWordsTable.TABLE + " SET " +
					JumbleWordsTable.SCORE+"="+JumbleWordsTable.SCORE+"+"+values.getAsInteger(JumbleWordsTable.ADDSCORE)
					+ " WHERE "+ selection, selectionArgs);
			return 1;
		case ADD_RATIO:
			db.execSQL(
					"UPDATE " + JumbleCategoryTable.TABLE + " SET " +
					JumbleCategoryTable.RATIO+"="+values.getAsInteger(JumbleCategoryTable.RATIO)
					+ " WHERE "+ selection, selectionArgs);
			return 1;
		case UNLOCK_CATEGORY:
			int rowChanged = db.update(JumbleCategoryTable.TABLE, values, selection, selectionArgs);
			/*db.execSQL(
					"UPDATE " + JumbleCategoryTable.TABLE + " SET " +
					JumbleCategoryTable.UNLOCK+"="+values.getAsInteger(JumbleCategoryTable.UNLOCK)
					+ " WHERE "+ selection, selectionArgs);*/
			Log.d(TAG, "unlock a category, row changed : "+rowChanged);
			return rowChanged;
		default:
			throw new IllegalArgumentException("Unsupported URI: " + uri);
		}

	}

}
