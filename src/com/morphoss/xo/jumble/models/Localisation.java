package com.morphoss.xo.jumble.models;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

public class Localisation implements Parcelable {

	public static String TAG = "Localisation";

	private String nameKey;
	private String word;
	private String countryCode;
	private String soundPath;
	private Difficulty level;

	@SuppressWarnings("unused")
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

	public boolean hasLocalisedWord() {
		return this.word != null;
	}

	public boolean hasLocalisedSound() {
		return this.soundPath != null;
	}

	public Difficulty hasLocalisedLevel() {
		return this.level;
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
