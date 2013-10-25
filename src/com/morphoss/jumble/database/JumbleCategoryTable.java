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

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class JumbleCategoryTable {
	/**
	 * This class is a the description of the table called scores
	 */
	// Version
	public static final int VERSION = 1;

	// Table name
	public static final String TABLE = "unlocked_categories";

	// Fields
	public static final String _ID = "_id";
	public static final String CATEGORY = "category";
	public static final String UNLOCK = "unlock";
	public static final String RATIO = "ratio";
	public static final String CC = "cc";

	// SQL to create table
	public static final String DATABASE_CREATE = "CREATE TABLE if not exists "
			+ TABLE + " (" + _ID + " integer PRIMARY KEY autoincrement,"
			+ CATEGORY + "," + UNLOCK + "," + RATIO + "," + CC + ")";

	public static void onCreate(SQLiteDatabase db) {
		db.execSQL(DATABASE_CREATE);
	}

	/**
	 * This method upgrades the table if a new version is published
	 * 
	 * @param db
	 * @param oldVersion
	 * @param newVersion
	 */
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
