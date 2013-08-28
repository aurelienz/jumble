package com.morphoss.xo.jumble.frontend;

import java.util.ArrayList;
import java.util.Collection;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.morphoss.xo.jumble.R;
import com.morphoss.xo.jumble.models.Category;

public class CategoryGridAdapter extends BaseAdapter {

	private Context context;
	private static ArrayList<Category> categories = null;;
	private int buttonWidth;
	private int buttonHeight;
	private int width;
	private int height;
	private int scoreToUnlock;
	private ImageView categoryImage;
	private Category category;
	private String status = "locked";

	private static final String TAG = "CategoryGridAdapter";

	public CategoryGridAdapter(Context context, int width, int height) {
		super();
		this.context = context;
		this.width = width;
		this.height = height;

	}

	public View getView(int position, View convertView, ViewGroup parent) {

		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		FrameLayout gridView;
		category = categories.get(position);
		gridView = (FrameLayout) inflater.inflate(R.layout.activity_category,
				null);
		gridView.setTag(category.getId());

		TextView textView = (TextView) gridView
				.findViewById(R.id.grid_item_label);

		textView.setText(category.getLocalisedName(context));

		categoryImage = (ImageView) gridView.findViewById(R.id.grid_item_image);

		LayoutParams lp = new LayoutParams(buttonWidth, buttonHeight,
				Gravity.FILL);
		gridView.setLayoutParams(lp);
		lockLevels();

		return gridView;
	}

	public void setCategories(Collection<Category> categories) {
		this.categories = new ArrayList<Category>();
		this.categories.addAll(categories);
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

	public static Category getCategory(int position) {
		return categories.get(position);
	}

	public void lockLevels() {
		scoreToUnlock = category.getMinScore();
		Log.d(TAG, "score to unlock level :" + scoreToUnlock);
		if (MainActivity.scoreTotal >= scoreToUnlock) {
			// unlock level
			categoryImage.setImageDrawable(category.getImage(context));
			status = "unlocked";
		}
	}

}
