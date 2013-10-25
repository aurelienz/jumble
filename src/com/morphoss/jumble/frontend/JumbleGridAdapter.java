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
package com.morphoss.jumble.frontend;

import java.util.ArrayList;

import com.morphoss.jumble.R;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class JumbleGridAdapter extends BaseAdapter {

	/**
	 * This class is an adapter for the JumbleActivity
	 */
	public static final String TAG = "JumbleGridAdapter";

	private ArrayList<View> contents;

	public JumbleGridAdapter(Context context, ArrayList<View> initialViews) {
		super();
		contents = new ArrayList<View>();
		for (int i = 0; i < initialViews.size(); i++)
			contents.add(initialViews.get(i));

	}

	/**
	 * This method get the view of the word
	 */
	public View getView(int position, View convertView, ViewGroup parent) {

		return contents.get(position);
		// Return the correct view or the convertview if unchanged
	}

	public boolean contains(TextView view) {

		for (View v : contents) {
			TextView myView = (TextView) v.findViewById(R.id.letter);
			if (myView == view)
				return true;

		}
		return false;

	}

	/**
	 * This method tests if the answer is correct
	 * 
	 * @param wordOnGuessView
	 * @param word
	 * @return
	 */
	public boolean TestAnswer(String wordOnGuessView, String word) {

		if (wordOnGuessView.equals(word)) {
			return true;
		} else {
			return false;
		}

	}

	@Override
	public int getCount() {
		return contents.size();
	}

	@Override
	public Object getItem(int position) {
		return contents.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

}