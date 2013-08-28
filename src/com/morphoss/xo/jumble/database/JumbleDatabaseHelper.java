package com.morphoss.xo.jumble.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class JumbleDatabaseHelper extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "usage";
	private static final int DATABASE_VERSION = 3;

	public JumbleDatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		JumbleWordsTable.onCreate(db);
		JumbleScoresTable.onCreate(db);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		JumbleWordsTable.onUpgrade(db, oldVersion, newVersion);
		JumbleScoresTable.onUpgrade(db, oldVersion, newVersion);
	}
}
