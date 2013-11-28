package com.puzzle;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;

public final class PicturePuzzleActivity extends Activity {

	public Level level;
	public DatabaseManager db;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		//configure database for first time use
		level = new Level();
		db = new DatabaseManager(this);
		if (!db.isTableExists()) {
			for (int i = 1; i <= 5; i++) {
				db.addRow("" + level.getScore(i - 1));
			}
		}
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnClassic: {
			// Perform Classic action on click
			Intent intent = new Intent(PicturePuzzleActivity.this,
					ClassicMainActivity.class);
			// pass level object to new intent/activity
			intent.putExtra("level", level);
			startActivity(intent);
			break;
		}
		case R.id.btnRelax: {
			// Perform Relax action on click
			Intent intent = new Intent(PicturePuzzleActivity.this,
					RelaxImageActivity.class);
			startActivity(intent);
			break;
		}
		case R.id.btnTimeAttack: {
			// Perform Time Attack action on click
			Intent intent = new Intent(PicturePuzzleActivity.this,
					TimeAttackMainActivity.class);
			intent.putExtra("level", level);
			startActivity(intent);
			break;
		}
		}
	}
}