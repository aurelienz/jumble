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

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.morphoss.jumble.R;

public class AvatarGridAdapter extends BaseAdapter {
	/**
	 * This class is an adapter for the avatar gridview
	 */
	private Context context;
	private static final int[] avatarArray = { R.drawable.avatar1,
			R.drawable.avatar2, R.drawable.avatar3, R.drawable.avatar4,
			R.drawable.avatar5, R.drawable.avatar6, R.drawable.avatar7,
			R.drawable.avatar8, R.drawable.avatar9, R.drawable.avatar10,
			R.drawable.avatar11

	};
	private static final String TAG = "AvatarGridAdapter";
	private int currentAvatar = 0;
	private View currentAvatarView = null;

	public AvatarGridAdapter(Context context) {
		super();
		this.context = context;

	}

	/**
	 * This method get the avatar view from the gridview
	 */
	public View getView(int position, View convertView, ViewGroup parent) {

		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		LinearLayout avatarLayout;
		avatarLayout = (LinearLayout) inflater.inflate(R.layout.each_avatar,
				null);
		avatarLayout.setTag(position);

		ImageView avatarImage = (ImageView) avatarLayout
				.findViewById(R.id.avatar);

		avatarImage.setImageDrawable(context.getResources().getDrawable(
				avatarArray[position]));
		avatarImage.setTag(position);
		avatarImage.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (currentAvatarView != null) {
					currentAvatarView
							.setBackgroundResource(R.drawable.blankview);
				}
				currentAvatarView = v;
				currentAvatarView
						.setBackgroundResource(R.drawable.shadow_avatar);
				currentAvatar = (Integer) v.getTag();

			}
		});

		return avatarLayout;
	}

	@Override
	public int getCount() {
		return avatarArray.length;
	}

	@Override
	public Object getItem(int position) {
		return avatarArray[position];
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	/**
	 * 
	 * @return the index of the selected avatar
	 */
	public int getCurrentAvatar() {
		return avatarArray[currentAvatar];
	}

}
