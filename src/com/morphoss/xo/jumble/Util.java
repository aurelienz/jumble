package com.morphoss.xo.jumble;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;

public class Util {

	public static String TAG = "Util";

	public static BitmapDrawable getImage(Context context, String imagePath) {
		FileInputStream stream = null;
		try {
			stream = new FileInputStream(new File(Constants.storagePath
					+ File.separator + imagePath));
			return new BitmapDrawable(context.getApplicationContext()
					.getResources(), stream);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

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
