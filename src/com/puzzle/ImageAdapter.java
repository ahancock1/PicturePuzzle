package com.puzzle;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ImageAdapter extends BaseAdapter {
	private Context mContext;

	private Level level = new Level();

	private int[] mImageIds;
	private boolean displayScore = false;

	public ImageAdapter(Context c, int[] mImageIds, boolean displayScore) {
		mContext = c;
		this.mImageIds = mImageIds;
		this.displayScore = displayScore;

		TypedArray attr = mContext
				.obtainStyledAttributes(R.styleable.MenuGallery);
		attr.recycle();
	}

	public int getCount() {
		return mImageIds.length;
	}

	public Object getItem(int position) {
		return position;
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {

		LinearLayout linearLayout = new LinearLayout(mContext);
		linearLayout.setOrientation(LinearLayout.VERTICAL);

		ImageView imageView = new ImageView(mContext);

		imageView.setImageResource(mImageIds[position]);
		imageView.setLayoutParams(new Gallery.LayoutParams(280, 315));
		imageView.setScaleType(ImageView.ScaleType.FIT_XY);
		imageView.setPadding(45, 20, 45, 20);
		imageView.setBackgroundResource(Color.TRANSPARENT);

		linearLayout.addView(imageView);	

		return linearLayout;
	}
}