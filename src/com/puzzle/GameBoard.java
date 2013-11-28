package com.puzzle;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.animation.AnimationUtils;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class GameBoard {

	private static GameBoard board = null; // Singleton instance - can be
	// changed by calling
	// createGameBoard class method

	public TileShuffle tileShuffle;
	public int[] shuffleArray = null;

	private List<Tile> tiles = null; 
	private List<TileView> tileViews = null;
	private List<TableRow> tableRow = null;
	public Tile theBlankTile; // The empty square
	public Bitmap bitmap; // Picture used for puzzle
	public TableLayout childLayout;
	public Boolean solved = false;

	public int gridSizeHeight;
	public int gridSizeWidth;

	public Context context;
	public int boardWidth; // pixel count
	public int boardHeight; // pixel count
	public int moveCount; // number of tile clicks from the user (score)

	public int tile_width;
	public int tile_height;

	public int level;

	/*
	 * (non-Javadoc) Private constructor to force access to class instance
	 * through createGameBoard method.
	 */
	private GameBoard(Context context, Bitmap bitmap, TableLayout parentLayout,
			int width, int height, int gridSizeWidth, int gridSizeHeight,
			int level) {
		this.context = context;
		this.boardWidth = width;
		this.boardHeight = height;
		this.bitmap = bitmap;
		this.moveCount = 0;
		this.childLayout = parentLayout;

		this.gridSizeWidth = gridSizeWidth;
		this.gridSizeHeight = gridSizeHeight;

		this.level = level;

		init();
	}

	/**
	 * Creates an instance of GameBoard.
	 * 
	 * @param context
	 * @param bitmap
	 *            The picture to be used for the puzzle
	 * @param parentLayout
	 *            The primary table layout for storing TileViews
	 * @param width
	 *            The board width in pixels
	 * @param height
	 *            The board height in pixels
	 * @param gridSize
	 *            The row and column count. (3 = 3x3, 4 = 4x4, etc.)
	 * @return an instance of the GameBoard that will be used for game play.
	 */
	public static GameBoard createGameBoard(Context context, Bitmap bitmap,
			TableLayout parentLayout, int width, int height, int gridSizeWidth,
			int gridSizeHeight, int level) {

		board = new GameBoard(context, bitmap, parentLayout, width, height,
				gridSizeWidth, gridSizeHeight, level);

		return board;
	}

	/*
	 * (non-Javadoc) Create tiles and views. Then shuffle.
	 */
	private void init() {
		initializeLists(); //sets the tiles array to null bitmap when new game is created
		createTiles();  //sets the bitmap background and size of tile depending on child layout height width and gridsize
		createTileViews(); //adds a set of tiles to an array to be added to the child table layout
		displayAllTiles(); //adds the array of tiles to the table layout
		shuffleTiles(); //shuffle tiles depending on level and fetches a required set of instructions for shuffling
	}

	// sets width, height, and bitmap
	public void setBitmap(Bitmap bitmap, int width, int height) {
		this.boardWidth = width;
		this.boardHeight = height;
		this.bitmap = bitmap;

	}

	//sets the grid size
	public void setGridSize(int gridSizeWidth, int gridSizeHeight) {
		this.gridSizeWidth = gridSizeWidth;
		this.gridSizeHeight = gridSizeHeight;
	}

	/*
	 * (non-Javadoc) Creates new objects for tiles, tile views, and table rows.
	 */
	private void initializeLists() {
		if (tiles == null) {
			tiles = new ArrayList<Tile>(gridSizeWidth * gridSizeHeight);
		} else {
			// Be sure to clean up old tiles
			for (int i = 0; i < tiles.size(); i++) {
				tiles.get(i).freeBitmap();
				tiles = new ArrayList<Tile>(gridSizeWidth * gridSizeHeight);
			}
		}
		tileViews = new ArrayList<TileView>(gridSizeWidth * gridSizeHeight);
		tableRow = new ArrayList<TableRow>(gridSizeHeight); // 2

		for (int row = 0; row < gridSizeHeight; row++) {
			tableRow.add(new TableRow(context));
		}
	}

	/*
	 * (non-Javadoc) Cut the picture into pieces and assign it to tiles. uses
	 * bitmap, gridsize, tiles array for the tiles
	 */
	private void createTiles() {
		bitmap = Bitmap.createScaledBitmap(bitmap, (boardWidth), (boardHeight),
				true);

		tile_width = (bitmap.getWidth() / gridSizeWidth); // 
		tile_height = (bitmap.getHeight() / gridSizeHeight); //
		
		for (int row = 0; row < gridSizeHeight; row++) { // 2
			for (int column = 0; column < gridSizeWidth; column++) { // 3
				Bitmap bm = Bitmap.createBitmap(bitmap, column * tile_width,
						row * tile_height, tile_width, tile_height);

				// if final, Tile -> blank
				if (row == gridSizeHeight - 1 && column == gridSizeWidth - 1) {

					bm = Bitmap.createBitmap(bitmap, column * tile_width, row
							* tile_height, tile_width, tile_height);
					bm.eraseColor(Color.BLACK);
					theBlankTile = new Tile(bm, row, column);
					tiles.add(theBlankTile);

				} else {
					tiles.add(new Tile(bm, row, column));
				}
			} // end column
		} // end row
	}

	/*
	 * (non-Javadoc) Initialize the tile views and add them to the table layout.
	 */
	private void createTileViews() {
		for (int row = 0; row < gridSizeHeight; row++) { // 2
			for (int column = 0; column < gridSizeWidth; column++) { // 3
				TileView tv = new TileView(context, row, column);
				tileViews.add(tv);
				tableRow.get(row).addView(tv);
			} // end column
			childLayout.addView(tableRow.get(row));
		} // end row
	}

	/**
	 * Re-arrange the tiles into a solvable puzzle.
	 */
	public void shuffleTiles() {

		tileShuffle = new TileShuffle(level);
		shuffleArray = tileShuffle.getArray();
		displayAllTiles();
		TileView tileView = null; // random tile
		int i = 0;

		for (i = 0; i < shuffleArray.length; i++) {
			tileView = tileViews.get(shuffleArray[i]);
			// tileViewUpdate(tileView);

			Tile tile = tileView.getCurrentTile();

			TileView theBlankTileView = tileViews
					.get(computeLocationValue(theBlankTile.getCurrentLocation()));
			theBlankTileView.setCurrentTile(tile);
			tileView.setCurrentTile(theBlankTile);
			// update tile with new position

		}
		moveCount = 0;

	}

	public int getMinMoves() {
		return shuffleArray.length;
	}

	public void displayAllTiles() {
		tiles.remove(theBlankTile);
		tiles.add(theBlankTile);

		for (int row = 0; row < gridSizeHeight; row++) {
			for (int column = 0; column < gridSizeWidth; column++) {
				tileViews.get(row * gridSizeWidth + column).setCurrentTile(
						tiles.get(row * gridSizeWidth + column));
			}
		}
	}

	/**
	 * Notifies the game board that a tile view has been touched. Typically only
	 * called by the TileViews.
	 * 
	 * @param tv
	 *            the TileView that was touched.
	 */
	public static void notifyTileViewUpdate(TileView tv) {
		board.tileViewUpdate(tv);
	}

	/*
	 * (non-Javadoc) Updates the board when the specified TileView has changed
	 * by swapping its position with the empty square.
	 * 
	 * @param tv the TileView that was touched
	 */
	private void tileViewUpdate(TileView tv) {
		swapTileWithBlank(tv);
	}

	/**
	 * Get the current "score"
	 * 
	 * @return the number of tile moves
	 */
	public int getMoveCount() {
		return moveCount;
	}

	
	/*
	 * (non-Javadoc) Determine if the entire board is correctly solved by all of
	 * the tiles being in the correct location.
	 */
	private boolean isCorrect() {
		// if a single tile is incorrect, return false
		for (Tile tile : tiles) {
			if (!tile.isCorrect()) {
				return false;
			}
		}
		return true;
	}

	/*
	 * (non-Javadoc) Determine if the tile view clicked is adjacent to the blank
	 * tile. If so, swap their locations. If this swap solves the puzzle,
	 * congratulate the user on being the smartest person in the world (or
	 * insult them for taking so many moves).
	 */
	private void swapTileWithBlank(TileView tv) {

		if (!solved) {
			Tile tile = tv.getCurrentTile();

			TileView theBlankTileView = tileViews
					.get(computeLocationValue(theBlankTile.getCurrentLocation()));

			if (tile.getCurrentLocation().isAdjacent(
					theBlankTile.getCurrentLocation())) {

				// Animate tile movement
				if (tile.getCurrentLocation().getColumn() < theBlankTile
						.getCurrentLocation().getColumn()) {
					theBlankTileView.bringToFront();
					// LEFT
					theBlankTileView
							.startAnimation(AnimationUtils.loadAnimation(
									this.context, R.anim.left_animation));

				} else if (tile.getCurrentLocation().getColumn() > theBlankTile
						.getCurrentLocation().getColumn()) {
					theBlankTileView.bringToFront();
					// RIGHT
					theBlankTileView
							.startAnimation(AnimationUtils.loadAnimation(
									this.context, R.anim.right_animation));

				} else if (tile.getCurrentLocation().getRow() < theBlankTile
						.getCurrentLocation().getRow()) {
					theBlankTileView.bringToFront();
					// UP
					theBlankTileView.startAnimation(AnimationUtils
							.loadAnimation(this.context, R.anim.up_animation));

				} else if (tile.getCurrentLocation().getRow() > theBlankTile
						.getCurrentLocation().getRow()) {
					theBlankTileView.bringToFront();
					// DOWN
					theBlankTileView
							.startAnimation(AnimationUtils.loadAnimation(
									this.context, R.anim.down_animation));
				}
				theBlankTileView.setCurrentTile(tile);
				tv.setCurrentTile(theBlankTile);
				moveCount++;
			}

		}
		if (isCorrect()) {
			isComplete();
		}

	}
	
	/*
	 * display blank tile as a real tile.
	 */
	private void isComplete() {
		
		Bitmap bm = Bitmap.createBitmap(bitmap, theBlankTile
				.getCorrectLocation().getColumn() * tile_width, theBlankTile
				.getCorrectLocation().getRow() * tile_height, tile_width,
				tile_height);

		theBlankTile.freeBitmap();
		theBlankTile.setBitmap(bm);
		displayAllTiles();
		solved = true;
		((Activity)context).showDialog(GameActivity.DIALOG_COMPLETED_ID);
		
	}

	/*
	 * (non-Javadoc) Return the location on the board for the given row and
	 * column, in the range 0 to gridSize-1. For instance, on a 4x4 grid the 2nd
	 * row 2nd column should have the value 5.
	 */
	private int computeLocationValue(int row, int column) {
		return (int) (gridSizeWidth * row + column);
	}

	/*
	 * (non-Javadoc) Return the location on the board for the given row and
	 * column, in the range 0 to gridSize-1. For instance, on a 4x4 grid the 2nd
	 * row 2nd column should have the value 5.
	 */
	private int computeLocationValue(TileLocation location) {
		return computeLocationValue(location.getRow(), location.getColumn());
	}

	/**
	 * Sets the visibility of the titles for the tiles.
	 * 
	 * @param visible
	 *            True if the tile's correct location should be displayed.
	 *            False, otherwise.
	 */
	public void setNumbersVisible(boolean visible) {
		for (TileView tv : tileViews) {
			tv.setNumbersVisible(true);
		}
	}
}