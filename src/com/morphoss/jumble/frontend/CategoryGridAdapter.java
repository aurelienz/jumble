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
import java.util.Collection;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.morphoss.jumble.R;
import com.morphoss.jumble.models.Category;

public class CategoryGridAdapter extends BaseAdapter {

	/**
	 * This class is an adapter for the categor gridview
	 */
	private final Context context;
	private static ArrayList<Category> categories = null;;
	private int buttonWidth;
	private int buttonHeight;
	private final int width;
	private final int height;
	private int scoreToUnlock;
	private ImageView categoryImage;
	private TextView categoryLabel;
	private Category category;
	private static final String TAG = "CategoryGridAdapter";

	public CategoryGridAdapter(Context context, int width, int height) {
		super();
		this.context = context;
		this.width = width;
		this.height = height;

	}

	/**
	 * This method get the view of a specific category
	 */
	public View getView(int position, View convertView, ViewGroup parent) {

		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		category = categories.get(position);
		View categoryLayout = inflater.inflate(R.layout.activity_category, null);
        categoryLabel = (TextView) categoryLayout.findViewById(R.id.category_label);
        categoryImage = (ImageView) categoryLayout.findViewById(R.id.category_image);

		categoryLayout.setTag(category.getId());
		categoryLabel.setText(category.getLocalisedName(context));

		lockLevels();

		return categoryLayout;
	}

	public void setLayout( View v ) {
        ViewGroup.LayoutParams lp = v.getLayoutParams();
        lp.height = buttonHeight;
        lp.width = buttonWidth;
        v.setLayoutParams(lp);
	}


	/**
	 * This method sets 8 categories on the screen
	 *
	 * @param categories
	 */
	public void setCategories(Collection<Category> categories) {
		CategoryGridAdapter.categories = new ArrayList<Category>();
		CategoryGridAdapter.categories.addAll(categories);
		if (width > height) {
			buttonWidth = width / 4;
			buttonHeight = height / 2;
		} else {
			buttonWidth = width / 2;
			buttonHeight = height / 4;
		}

		this.notifyDataSetInvalidated();
		this.notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		if (categories == null)
			return 0;
		return categories.size();
	}

	@Override
	public Object getItem(int position) {
		return categories.get(position);
	}

	@Override
	public long getItemId(int position) {
		return categories.get(position).getId();
	}

	/**
	 * this method gets the current category
	 *
	 * @param position
	 * @return the category selected
	 */
	public static Category getCategory(int position) {
		return categories.get(position);
	}

	/**
	 * This method locks and unlocks levels
	 */
	public void lockLevels() {
		scoreToUnlock = category.getMinScore();
		Log.d(TAG, "score to unlock level :" + scoreToUnlock);
		if (MainActivity.scoreTotal >= scoreToUnlock) {
			// unlock level
			categoryImage.setImageDrawable(category.getImage(context));
		}
	}

}
