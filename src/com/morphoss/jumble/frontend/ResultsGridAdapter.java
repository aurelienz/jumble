package com.morphoss.jumble.frontend;

import java.util.ArrayList;
import java.util.Collection;

import com.morphoss.jumble.R;
import com.morphoss.jumble.models.Category;

import android.content.Context;
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
	private static final int[] categoriesArray = { R.drawable.animals,
			R.drawable.fruits_vegetables, R.drawable.home, R.drawable.nature,
			R.drawable.colors, R.drawable.clothes, R.drawable.health,
			R.drawable.sports

	};
	private static final String TAG = "AvatarGridAdapter";
	private int currentCategory = 0;
	private View currentCategoryView = null;
	private static String[] categories = { "Animals", "Fruits and Vegetables",
			"Home", "Nature", "Colors", "Clothes", "Health", "Sports" };

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

		LinearLayout categoryLayout;
		categoryLayout = (LinearLayout) inflater.inflate(R.layout.gallery_category,
				null);
		categoryLayout.setTag(position);

		ImageView categoryImage = (ImageView) categoryLayout
				.findViewById(R.id.category_view);

		categoryImage.setImageDrawable(context.getResources().getDrawable(
				categoriesArray[position]));
		categoryImage.setTag(position);
		TextView categoryName = (TextView) categoryLayout.findViewById(R.id.category_name);
		categoryName.setText(categories[position]);
		categoryImage.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (currentCategoryView != null) {
					currentCategoryView
							.setBackgroundResource(R.drawable.blankview);
				}
				currentCategoryView = v;
				currentCategoryView
						.setBackgroundResource(R.drawable.shadow_avatar);
				currentCategory = (Integer) v.getTag();

			}
		});

		return categoryLayout;
	}

	public int getCount() {
		return categoriesArray.length;
	}

	public Object getItem(int position) {
		return categoriesArray[position];
	}

	public long getItemId(int position) {
		return position;
	}

	/**
	 * 
	 * @return the index of the selected category
	 */
	public int getCurrentCategory() {
		return categoriesArray[currentCategory];
	}
}
