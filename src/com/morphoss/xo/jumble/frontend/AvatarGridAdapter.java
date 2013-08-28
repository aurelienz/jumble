package com.morphoss.xo.jumble.frontend;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.morphoss.xo.jumble.R;

public class AvatarGridAdapter extends BaseAdapter {

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

	public int getCurrentAvatar() {
		return avatarArray[currentAvatar];
	}

}
