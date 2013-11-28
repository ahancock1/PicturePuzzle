package com.puzzle;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;

public class GameActivity extends Activity {

	public static final int DIALOG_COMPLETED_ID = 0; // remove or use later

	public GameBoard gameboard;
	public Level level;

	public Uri targetUri;
	public Bitmap bitmap;
	public int gamelevel;
	public GameSetting gameSetting;
	public int gameType;
	public int childWidth;
	public int childHeight;

	public int[] gameScores;

	public TextView txtMove;

	// SQL SHIT
	public DatabaseManager db;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.board);

		db = new DatabaseManager(this);

		/*
		 * Get passed variables from the previous activity and store them in
		 * this activity
		 */
		gameType = getIntent().getIntExtra("gameType", -1);
		targetUri = (Uri) getIntent().getParcelableExtra("targetUri");
		level = (Level) getIntent().getSerializableExtra("level");

		gamelevel = level.getLevel();
		gameScores = level.setScore(gamelevel, level.getScore(gamelevel));
		setBest();
		txtMove = (TextView) findViewById(R.id.txtMove);
		txtMove.setText("0");

		// get game settings
		gameSetting = new GameSetting(gamelevel);

		// display best score onscreen
		setBest();

		// images for puzzle
		Integer[] mPuzzleImages = { R.drawable.cherryblossom,
				R.drawable.colosseum, R.drawable.fruits,
				R.drawable.goldengatebridge, R.drawable.pisatower,
				R.drawable.pyramid_sphinx, R.drawable.smeatonstower,
				R.drawable.stonehenge, R.drawable.tajmahal,
				R.drawable.windmill_tulips };

		// random generator for selecting a random image to create a puzzle from
		Random random = new Random();
		int randomSelector = random.nextInt((mPuzzleImages.length) - 1);

		// get child table layout measurments for scaling the picture to
		getChildLayoutSize();

		/*
		 * create puzzle and settings depending on which game was selected
		 */
		switch (gameType) {
		case 0: {
			/*
			 * CLASSIC GAME
			 */
			// get random image
			bitmap = BitmapFactory.decodeResource(getResources(),
					mPuzzleImages[randomSelector]);

			// create picture puzzle from random image
			createGameBoard(bitmap, gamelevel);
			break;
		}
		case 1: {
			/*
			 * RELAX GAME
			 */
			try {
				InputStream is = (getContentResolver()
						.openInputStream(targetUri));
				Bitmap relaxBitmap = decodeBitmap(is);
				createGameBoard(relaxBitmap, gamelevel);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (@SuppressWarnings("hiding") IOException e) {
				e.printStackTrace();
			}

			break;
		}
		case 2: {
			/*
			 * TIME ATTACK GAME
			 */
			bitmap = BitmapFactory.decodeResource(getResources(),
					mPuzzleImages[randomSelector]);
			// create picture puzzle from random image
			createGameBoard(bitmap, gamelevel);
			break;
		}
		}

	}

	/*
	 * write scores to database by updating the levelscore with top score ONLY
	 * top score
	 */
	public void updateDatabase() {
		try {
			// ask the database manager to update the row based on the
			// information
			// found in the corresponding user entry fields
			int thislevel = gamelevel + 1;
			db.updateRow(Long.parseLong(("" + thislevel).toString()), ""
					+ gameboard.getMoveCount());
		} catch (Exception e) {
			Log.e("Update Error", e.toString());
			e.printStackTrace();
		}
	}

	/**
	 * displays the number of moves made. Gets incremented move count from
	 * GameBoard class
	 */
	public void updateMove() {
		txtMove.setText("" + gameboard.getMoveCount());
	}

	/*
	 * performs actions depending on which button has been pressed
	 */
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnRestart: {
			// reset game
			reset();
			break;
		}
		case R.id.btnMenu: {
			// close activity and display previous activity
			finish();
			break;
		}
		case R.id.childLayout: {
			// Perform update of move count when a tile is pressed
			updateMove();
			break;
		}
		case R.id.btnDialogRestart: {
			// reset game
			reset();
		}
		}
	}

	/*
	 * perform reset tiles, move count, and game solved.
	 */
	public void reset() {
		if (!gameboard.solved) {
			// reset puzzle tiles if game is not complete
			gameboard.shuffleTiles();
		} else {
			// GAME IS COMPLETE
			// update the onscreen best score
			setBest();
			updateDatabase();
			// reset game with blanktile and shuffle tiles
			createGameBoard(bitmap, gamelevel);
		}
		/*
		 * reset number of moves and move count to zero. set game to not solved.
		 */
		txtMove.setText("0");
		gameboard.solved = false;
		gameboard.moveCount = 0;
	}

	/*
	 * Set the sample size for the image chosen from the phones gallery
	 */
	private Bitmap decodeBitmap(InputStream is) throws FileNotFoundException {

		// decode with inSampleSize
		// sample the image taken from phone gallery
		BitmapFactory.Options o2 = new BitmapFactory.Options();
		o2.inSampleSize = 4;
		return BitmapFactory.decodeStream(is, null, o2);
	}

	/**
	 * get the layout parameters of the child table layout to re-size the image
	 * to
	 */
	private void getChildLayoutSize() {
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		childWidth = (int) ((metrics.widthPixels * metrics.density) - (94 * metrics.density));
		childHeight = (int) ((metrics.heightPixels * metrics.density) - (64 * metrics.density));
	}

	/**
	 * creates the game screen and displays it in the child table layout. Takes
	 * puzzle image and settings required to size the picture puzzle, and level
	 * settings.
	 * 
	 * @param bitmap
	 *            Image used to create puzzle
	 */
	private final void createGameBoard(Bitmap bitmap, int gamelevel) {
		TableLayout tableLayout;
		tableLayout = (TableLayout) findViewById(R.id.childLayout);
		tableLayout.removeAllViews();

		gameboard = GameBoard.createGameBoard(this, bitmap,
				// resizeBitmap(bitmap),
				tableLayout, childWidth, childHeight, gameSetting.getWidth(),
				gameSetting.getHeight(), gamelevel);
	}

	/**
	 * sets the score once the game is completed with the number of moves made
	 */
	public void setScore() {
		Integer moveCount = new Integer(gameboard.getMoveCount());
		gameScores = level.setScore(gamelevel, moveCount);
	}

	/**
	 * takes the number for moves made to complete the puzzle and returns the
	 * image resource location for the number of stars awarded.
	 * 
	 * @param score
	 *            number of moves the puzzle was completed in
	 * @return The rating give in stars depending on the number of moves made to
	 *         complete the puzzle
	 */
	public int calculateScore(Integer score) {
		int stars = 0;
		if (score <= gameScores[0]) {
			// award 3 stars
			stars = R.drawable.stars_3;
		} else if (score <= gameScores[1]) {
			// award 2 stars
			stars = R.drawable.stars_2;
		} else if (score <= gameScores[2]) {
			// award 1 star
			stars = R.drawable.stars_1;
		} else {
			// award no stars
			stars = R.drawable.stars_0;
		}
		return stars;
	}

	/**
	 * sets the best score for the level onscreen in textview and imageview
	 */
	public void setBest() {
		TextView txtBest = (TextView) findViewById(R.id.txtBestScore);
		txtBest.setText("" + level.getScore(gamelevel));
		ImageView imgBest = (ImageView) findViewById(R.id.imgBestScore);
		if (level.getScore(gamelevel) != 0) {
			imgBest.setImageResource(calculateScore(level.getScore(gamelevel)));
		}
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		Dialog dialog = new Dialog(GameActivity.this);
		switch (id) {
		case DIALOG_COMPLETED_ID:
			dialog = gameCompletedDialog(dialog);
			setScore();
			break;
		default:
			dialog = null;
		}
		// setBest();
		// gameboard.displayAllTiles();
		return dialog;
	}

	// button dialog
	public Dialog gameCompletedDialog(final Dialog dialog) {
		dialog.setContentView(R.layout.custom_dialog);
		dialog.setTitle("Congratulations");

		Button btnreset = (Button) dialog.findViewById(R.id.btnDialogRestart);
		btnreset.setOnClickListener(new View.OnClickListener() {
			// RESTART BUTTON
			public void onClick(View v) {
				reset();
				dialog.dismiss();
			}
		});
		Button btnNext = (Button) dialog.findViewById(R.id.btnDialogNext);
		btnNext.setOnClickListener(new View.OnClickListener() {
			// Next BUTTON
			public void onClick(View v) {
				// createGameBoard(bitmap, gamelevel + 1);
				// dialog.dismiss();
			}
		});
		return dialog;
	}

	// change moves made and stars awarded depending on moves made to complete
	// the puzzle
	@Override
	protected void onPrepareDialog(int id, Dialog dialog) {
		switch (id) {
		case DIALOG_COMPLETED_ID:
			// setScore();
			final TextView text = (TextView) dialog.findViewById(R.id.txtBody);
			text.setText("You completed the level in "
					+ gameboard.getMoveCount() + " moves and earnt");
			ImageView image = (ImageView) dialog.findViewById(R.id.image);
			image.setImageResource(calculateScore(gameboard.getMoveCount()));
			// setBest();

			break;
		}
	}

}