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
package com.morphoss.jumble.models;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;

public class Localisation implements Parcelable {

	/**
	 * This class sets all the details about localisation
	 */
	public static String TAG = "Localisation";

	private String nameKey;
	private String word;
	private String countryCode;
	private String soundPath;
	private Difficulty level;

	private Localisation(String nameKey, String cc, JSONObject data)
			throws JSONException {
		this.nameKey = nameKey;

		if (data.has("word"))
			this.word = data.getString("word");
		if (data.has("sound"))
			this.soundPath = data.getString("sound");
		if (data.has("level"))
			this.level = Difficulty.getDifficulty(data.getInt("level"));
		this.countryCode = cc;

		if (countryCode == null)
			throw new NullPointerException("Country code should not be null!");
	}

	public Localisation(Parcel in) {
		readFromParcel(in);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	/**
	 * This method checks if the word has an other localization
	 *
	 * @return
	 */
	public boolean hasLocalisedWord() {
		return this.word != null;
	}

	/**
	 * This method checks if the sound of the word has an other localization
	 *
	 * @return
	 */
	public boolean hasLocalisedSound() {
		return this.soundPath != null;
	}

	/**
	 * This method checks if the difficulty of the word has an other
	 * localization
	 *
	 * @return
	 */
	public boolean hasLocalisedLevel() {
		return this.level != null;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(nameKey);
		dest.writeString(word);
		dest.writeString(countryCode);
		dest.writeString(soundPath);
	}

	private void readFromParcel(Parcel in) {
		nameKey = in.readString();
		word = in.readString();
		soundPath = in.readString();
		countryCode = in.readString();
		if (countryCode == null)
			throw new NullPointerException("Country code should not be null!");
	}

	public static final Parcelable.Creator<Localisation> CREATOR = new Parcelable.Creator<Localisation>() {
		public Localisation createFromParcel(Parcel in) {
			return new Localisation(in);
		}

		public Localisation[] newArray(int size) {
			return new Localisation[size];
		}
	};

	public String getNameKey() {
		return nameKey;
	}

	public void setName_key(String nameKey) {
		this.nameKey = nameKey;
	}

	public String getLocalisedWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public String getLocalisedSoundPath() {
		return soundPath;
	}

	public void setSoundPath(String soundPath) {
		this.soundPath = soundPath;
	}

	public Difficulty getLocalisedLevel() {
		return level;
	}

	/**
	 * This method gets all the infomations about localization from the JSON
	 * file
	 *
	 * @param json
	 * @return
	 * @throws JSONException
	 */
	public static HashMap<String, ArrayList<Localisation>> getLocalisationFromJson(
			JSONObject json) throws JSONException {
		HashMap<String, ArrayList<Localisation>> localisations = new HashMap<String, ArrayList<Localisation>>();
		JSONArray keys = json.names();
		for (int i = 0; i < keys.length(); i++) {
			String cc = (String) keys.get(i);
			JSONObject data = json.getJSONObject(cc);

			ArrayList<Localisation> current = new ArrayList<Localisation>();
			JSONArray words = data.names();
			JSONArray sounds = data.names();
			for (int j = 0; j < words.length(); j++) {
				String wordKey = (String) words.get(j);
				JSONObject value = data.getJSONObject(wordKey);
				current.add(new Localisation(wordKey, cc, value));
			}
			localisations.put(cc, current);
		}
		return localisations;
	}

}
