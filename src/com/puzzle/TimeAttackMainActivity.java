package com.puzzle;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.Toast;

public class TimeAttackMainActivity extends Activity {

	Level level = new Level();

	private int[] mImageIds = { R.drawable.ic_top_scores_c,
			R.drawable.ic_scores1_t, R.drawable.ic_scores3_t,
			R.drawable.locked, R.drawable.locked, R.drawable.locked,
			R.drawable.locked, R.drawable.locked, R.drawable.locked,
			R.drawable.locked, R.drawable.locked };

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.time_attack_main); // change R.layout.*
													// depending on screen

		CustomGallery gallery = (CustomGallery) findViewById(R.id.gallery);
		gallery.setAdapter(new ImageAdapter(this, mImageIds, true));
		gallery.setSelection(1);

		Button btnBack = (Button) findViewById(R.id.btnBack);

		// set images

		gallery.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView parent, View v, int position,
					long id) {

				Intent intent = new Intent(TimeAttackMainActivity.this,
						GameActivity.class);
				
				intent.putExtra("gameType", 2);
				level.setLevel(position - 1);
				intent.putExtra("level", level);
				
				startActivity(intent);
			}
		});

		/*
		 * back button
		 */
		btnBack.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				// closes current activity
				finish();
			}
		});

	}
}
