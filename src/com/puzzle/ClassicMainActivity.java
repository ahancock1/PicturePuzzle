package com.puzzle;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

public final class ClassicMainActivity extends Activity {

	// icons images for the gallery and level buttons.
	private int[] mImageIds = new int[6];
	public Level level;
	public DatabaseManager db;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.classic_main);

		Intent intent = getIntent();
		level = (Level) intent.getSerializableExtra("level");
		db = new DatabaseManager(this);

		//used to set the icons in the gallery
		setGalleryIcons();

		//create custom gallery with the set icons
		CustomGallery gallery = (CustomGallery) findViewById(R.id.gallery);
		gallery.setAdapter(new ImageAdapter(this, mImageIds, true));
		gallery.setSelection(1);

		
		gallery.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView parent, View v, int position,
					long id) {
				//if level is locked the user cant play without completing the previous level
				if (mImageIds[position] == R.drawable.locked) {
					Toast msg = Toast.makeText(ClassicMainActivity.this,
							"This level needs to be unlocked!",
							Toast.LENGTH_LONG);
					msg.setGravity(Gravity.CENTER, msg.getXOffset() / 2, msg.getYOffset() / 2);
					msg.show();
				} else {
					//if level is not locked display the game screen activity
					Intent intent = new Intent(ClassicMainActivity.this,
							GameActivity.class);
					intent.putExtra("gameType", 0);
					level.setLevel(position - 1);
					intent.putExtra("level", level);

					startActivity(intent);
				}
			}
		});
	}

	/**
	 * set the gallery icons to unlocked or locked depending on the puzzle scores
	 * in the database.
	 */
	public void setGalleryIcons() {
		//store row from database containing level and level score.
		ArrayList<Object> row = new ArrayList<Object>();
		Integer rowScore = 0;
		//prescore used to set the next available playable level depending if the previous level has been completed
		Integer preRowScore = 0;
		//loop through all the levels tp be displayed in the gallery
		for (int i = 0; i < 6; i++) {
			if (i > 0) {
				row = db.getRowAsArray(i);
				preRowScore = rowScore;
				rowScore = (Integer) Integer.parseInt(row.get(1).toString());

			}
			switch (i) {
			case 0: {
				mImageIds[i] = R.drawable.ic_top_scores_c;
				break;
			}
			case 1: {
				mImageIds[i] = R.drawable.classic_1;
				level.setScore(i - 1, rowScore);
				break;
			}
			default: {

				if (preRowScore == 0) {
					mImageIds[i] = R.drawable.locked;
				} else {
					switch (i) {
					case 2: {
						mImageIds[i] = R.drawable.classic_2;
						level.setScore(i - 1, rowScore);
						break;
					}
					case 3: {
						mImageIds[i] = R.drawable.classic_3;
						level.setScore(i - 1, rowScore);
						break;
					}
					case 4: {
						mImageIds[i] = R.drawable.classic_4;
						level.setScore(i - 1, rowScore);
						break;
					}
					case 5: {
						mImageIds[i] = R.drawable.classic_5;
						level.setScore(i - 1, rowScore);
						break;
					}
					}
				}
				break;
			}
			}
		}
	}

	public void onClick(View v) {
		// closes current activity
		finish();
	}
}
