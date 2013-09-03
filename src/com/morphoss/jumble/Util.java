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
package com.morphoss.jumble;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;

public class Util {

	public static String TAG = "Util";
	public static String dirPath = null;;

	public static String createInternalStorage(Context context) {

		File mydir = context.getDir("jumble", Context.MODE_PRIVATE); // Creating
																		// an
		// internal directory called jumble;
		if (!mydir.exists()) {
			mydir.mkdirs();
		}
		dirPath = mydir.getAbsolutePath();

		return dirPath;

	}

	/**
	 * this method is getting this image from the SD card
	 *
	 * @param context
	 *            Context required to work
	 * @param imagePath
	 *            path of image
	 * @return Drawable representation of image
	 */
	public static BitmapDrawable getImage(Context context, String imagePath) {
		FileInputStream stream = null;
		try {
			stream = new FileInputStream(new File(createInternalStorage(context) + File.separator
					+ imagePath));
			return new BitmapDrawable(context.getApplicationContext()
					.getResources(), stream);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * this method get the string with useful informations (imagepath,
	 * soundpath) from a particular file
	 *
	 * @param input
	 * @return
	 * @throws IOException
	 */
	public static String getStringFromFile(File input) throws IOException {

		String ret = "";
		FileInputStream inputStream = new FileInputStream(input);

		if (inputStream != null) {
			InputStreamReader inputStreamReader = new InputStreamReader(
					inputStream);
			BufferedReader bufferedReader = new BufferedReader(
					inputStreamReader);
			String receiveString = "";
			StringBuilder stringBuilder = new StringBuilder();

			while ((receiveString = bufferedReader.readLine()) != null) {
				stringBuilder.append(receiveString);
			}

			inputStream.close();
			ret = stringBuilder.toString();
		}

		return ret;
	}
}
