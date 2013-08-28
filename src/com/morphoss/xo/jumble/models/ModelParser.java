package com.morphoss.xo.jumble.models;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class ModelParser {

	public static final String TAG = "ModelParser";

	public static Collection<Category> readJsonData(String json)
			throws IOException, JSONException {

		JSONObject file = new JSONObject(json);
		JSONObject cats = file.getJSONObject("categories");
		JSONObject Words = file.getJSONObject("words");
		HashMap<Integer, Category> catMap = Category
				.getCategoriesFromJson(cats);

		HashMap<String, Word> words = Word.getWordFromJson(Words, catMap);

		HashMap<String, ArrayList<Localisation>> localisations = Localisation
				.getLocalisationFromJson(file.getJSONObject("localisations"));

		for (String cc : localisations.keySet()) {
			ArrayList<Localisation> locs = localisations.get(cc);
			for (Localisation loc : locs) {
				Word word = words.get(loc.getNameKey());
				if (word != null) {
					word.addLocalisation(cc, loc);
				} else {
					Log.e(TAG, "Unable to find original word with name key: "
							+ loc.getNameKey());
				}
			}
		}

		return catMap.values();
	}
}
