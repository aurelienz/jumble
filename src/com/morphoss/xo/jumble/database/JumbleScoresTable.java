package com.morphoss.xo.jumble.database;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class JumbleScoresTable {
	// Version
	public static final int VERSION = 1;

	// Table name
	public static final String TABLE = "high_scores";

	// Fields
	public static final String _ID = "_id";
	public static final String SCORE = "score";
	public static final String CATEGORY = "category";
	public static final String CC = "cc";

	// SQL to create table
	public static final String DATABASE_CREATE = "CREATE TABLE if not exists "
			+ TABLE + " (" + _ID + " integer PRIMARY KEY autoincrement,"
			+ SCORE + " INTEGER," + CATEGORY + "," + CC + ")";

	public static void onCreate(SQLiteDatabase db) {
		db.execSQL(DATABASE_CREATE);
	}

	public static void onUpgrade(SQLiteDatabase db, int oldVersion,
			int newVersion) {
		if (oldVersion < 3) {
			Log.d("UsageTable", "Upgrading from " + oldVersion + " to "
					+ newVersion);
			db.execSQL("DROP TABLE IF EXISTS " + TABLE);
			onCreate(db);
			oldVersion = 3;
		}

	}
}
