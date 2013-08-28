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
package com.morphoss.xo.jumble.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.morphoss.xo.jumble.Util;
import com.morphoss.xo.jumble.frontend.SettingsActivity;

public class Word implements Parcelable {

	/**
	 * This class sets all the details about the Word type
	 */
	public static String TAG = "Word";

	private String nameKey;
	private String imagePath;
	private String soundPath;
	private Difficulty level;
	private HashMap<String, Localisation> localisations = new HashMap<String, Localisation>();

	public BitmapDrawable getImage(Context context) {
		return Util.getImage(context, imagePath);
	}

	private Word(String word, JSONObject data) throws JSONException {
		this.nameKey = word;
		this.imagePath = data.getString("image");
		this.soundPath = data.getString("sound");
		int l = data.getInt("level");
		this.level = Difficulty.getDifficulty(l);

	}

	public Difficulty getLevel() {
		return level;
	}

	public void setLevel(Difficulty level) {
		this.level = level;
	}

	public String getSoundPath() {
		return soundPath;
	}

	public Word(Parcel in) {
		readFromParcel(in);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	public String getNameKey() {
		return nameKey;
	}

	public void setNameKey(String nameKey) {
		this.nameKey = nameKey;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(nameKey);
		dest.writeString(imagePath);
		dest.writeString(soundPath);
		dest.writeValue(level);

		ArrayList<Localisation> valueList = new ArrayList<Localisation>();
		valueList.addAll(localisations.values());

		Localisation[] arr = new Localisation[valueList.size()];
		valueList.toArray(arr);
		dest.writeParcelableArray(arr, 0);

	}

	private void readFromParcel(Parcel in) {
		nameKey = in.readString();
		imagePath = in.readString();
		soundPath = in.readString();

		Parcelable[] arr = in.readParcelableArray(Localisation.class
				.getClassLoader());

		for (Parcelable p : arr)
			this.localisations.put(((Localisation) p).getCountryCode(),
					(Localisation) p);

	}

	public static final Parcelable.Creator<Word> CREATOR = new Parcelable.Creator<Word>() {
		public Word createFromParcel(Parcel in) {
			return new Word(in);
		}

		public Word[] newArray(int size) {
			return new Word[size];
		}
	};

	/**
	 * This method gets the word and its details like imagepath and soundpath
	 * from the JSON file
	 * 
	 * @param json
	 * @param categories
	 * @return
	 * @throws JSONException
	 */
	public static HashMap<String, Word> getWordFromJson(JSONObject json,
			HashMap<Integer, Category> categories) throws JSONException {
		Log.d(TAG, "creating ArrayList");
		HashMap<String, Word> words = new HashMap<String, Word>();
		JSONArray keys = json.names();
		for (int i = 0; i < keys.length(); i++) {
			String key = (String) keys.get(i);
			JSONObject data = json.getJSONObject(key);
			int catId = data.getInt("category");
			Word newWord = new Word(key, data);
			categories.get(catId).addWord(newWord);
			words.put(key, newWord);
		}
		return words;

	}

	public void addLocalisation(String cc, Localisation loc) {
		this.localisations.put(cc, loc);
	}

	public String toNiceString() {
		String ret = "Word - key: " + nameKey + " image: " + imagePath
				+ " sound: " + soundPath + " level: " + level;
		ret += "\n\t\tLocalisations: ";
		for (String cc : localisations.keySet())
			ret += cc + " ";
		return ret;
	}

	@Override
	public String toString() {
		return this.nameKey + "(" + this.level.toString() + ")";
	}

	/**
	 * 
	 * @param context
	 * @return the localized word if it exists
	 */
	public String getLocalisedWord(Context context) {
		String cc = SettingsActivity.getLanguageToLoad();
		Localisation loc = localisations.get(cc);
		if (loc != null && loc.hasLocalisedWord())
			return loc.getLocalisedWord();
		return nameKey;
	}

	/**
	 * 
	 * @param context
	 * @return the localized sound if it exists
	 */
	public String getLocalisedSound(Context context) {
		String cc = SettingsActivity.getLanguageToLoad();

		Localisation loc = localisations.get(cc);
		if (loc != null && loc.hasLocalisedSound())
			return loc.getLocalisedSoundPath();
		return soundPath;
	}

	/**
	 * 
	 * @param context
	 * @return the localized level if it exists
	 */
	public Difficulty getLocalisedLevel(Context context) {
		String cc = SettingsActivity.getLanguageToLoad();
		Localisation loc = localisations.get(cc);
		if (loc != null)
			return loc.hasLocalisedLevel();
		return level;
	}

	/**
	 * 
	 * @return the localization details
	 */
	public HashMap<String, Localisation> getLocalisations() {
		return localisations;
	}
}
