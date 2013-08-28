package com.morphoss.xo.jumble.frontend;

import java.util.ArrayList;

import com.morphoss.xo.jumble.R;
import com.morphoss.xo.jumble.R.id;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class TileGridAdapter extends BaseAdapter {

	public static final String TAG = "TileGridAdapter";

	private ArrayList<View> contents;

	public TileGridAdapter(Context context, ArrayList<View> initialViews) {
		super();
		contents = new ArrayList<View>();
		for (int i = 0; i < initialViews.size(); i++)
			contents.add(initialViews.get(i));

	}

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