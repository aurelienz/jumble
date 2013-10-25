package com.morphoss.jumble.frontend;

import java.util.ArrayList;
import java.util.Collection;

import com.morphoss.jumble.R;
import com.morphoss.jumble.database.JumbleCategoryTable;
import com.morphoss.jumble.database.JumbleProvider;
import com.morphoss.jumble.models.Category;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ResultsGridAdapter {
	/**
	 * This class is an adapter for the category gridview in ResultsActivity
	 */
	private Context context;

	private static final String TAG = "ResultsGridAdapter";
	private static ImageView categoryImage;
	private static TextView categoryLabel;
	private Category category;
	private static ArrayList<Category> categories = null;

	public ResultsGridAdapter(Context context) {
		super();
		this.context = context;

	}

	/**
	 * This method get the category view from the gridview
	 */
	public View getView(int position, View convertView, ViewGroup parent) {

		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		category = categories.get(position);
		View categoryLayout = inflater.inflate(R.layout.gallery_category, null);
		categoryLabel = (TextView) categoryLayout
				.findViewById(R.id.category_name);
		categoryImage = (ImageView) categoryLayout
				.findViewById(R.id.category_view);
		categoryImage.setImageDrawable(category.getImage(context));

		categoryLayout.setTag(category.getId());
		Log.d(TAG, "localised name : " + category.getLocalisedName());
		categoryLabel.setText(category.getLocalisedName());

		ContentValues myRow = new ContentValues();
		ContentResolver resolver = context.getContentResolver();
		String[] projection = { JumbleCategoryTable.RATIO };
		String selection = JumbleCategoryTable.CATEGORY + " = ? AND "
				+ JumbleCategoryTable.CC + " = ?";
		String[] selectionArgs = new String[] { category.getLocalisedName(),
				SettingsActivity.getLanguageToLoad() };
		Cursor cursor = resolver.query(JumbleProvider.CONTENT_URI_CATEGORIES,
				projection, selection, selectionArgs, null);
		for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {

			DatabaseUtils.cursorRowToContentValues(cursor, myRow);
			Log.d(TAG,
					"Got row :" + myRow.getAsString(JumbleCategoryTable.RATIO));
		}

		if (cursor.getCount() != 0
				&& !myRow.getAsString(JumbleCategoryTable.RATIO).equals("0")) {
			categoryLabel.setText(myRow.getAsString(JumbleCategoryTable.RATIO)
					+ " %");
		}

		return categoryLayout;
	}

	public void setCategories(Collection<Category> newCategories) {

		ResultsGridAdapter.categories = new ArrayList<Category>(
				newCategories.size());
		ResultsGridAdapter.categories.addAll(newCategories);

	}

	public int getCount() {
		if (categories == null)
			return 0;
		return categories.size();
	}

	public Object getItem(int position) {
		return categories.get(position);
	}

	public long getItemId(int position) {
		return categories.get(position).getId();
	}

	public void setLayout(View v) {
		ViewGroup.LayoutParams lp = v.getLayoutParams();
		v.setLayoutParams(lp);
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
}
