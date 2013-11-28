package com.puzzle;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.view.MotionEvent;
import android.widget.TextView;

public class TileView extends TextView {

	   private Tile currentTile; // tile to be displayed
	   private TileLocation myLocation; // permanent location on game board
	   private String title; // the current tile's correct location (changes)
	   private boolean numbersVisible = false; // should title be displayed - false
	   
	   /**
	    * Constructor for creating a TileView at the specified location on the game
	    * board.
	    * @param context
	    * @param row This TileView's location (index starts at 0)
	    * @param column This TileView's location (index starts at 0)
	    */
	   public TileView(Context context, int row, int column) {
	      super(context);
	      this.myLocation = new TileLocation(row, column);
	      super.setCursorVisible(false);
	      super.setTypeface(Typeface.DEFAULT_BOLD);
	      super.setTextColor(Color.RED); 
	   }
	   
	   @Override
	   public boolean onTouchEvent(MotionEvent event) {
	      GameBoard.notifyTileViewUpdate(this);
	      return super.onTouchEvent(event);
	   }   
	   
	   /**
	    * Is this TileView the place on the game board that the current tile should
	    * reside? when the tile has been moved to its right place, return true.
	    * @return true if the current tile is correct
	    */
	   public boolean isTileCorrect() {
	      return currentTile.isCorrect();
	   }
	   
	   /**
	    * Make this TileView display the specified Tile by setting the title to the
	    * Tile's correct location and the background image to the Tile's bitmap.
	    * @param tile The tile to display
	    */
	   public void setCurrentTile(Tile tile) {   
	      this.currentTile = tile;
	      super.setBackgroundDrawable(new BitmapDrawable(tile.getBitmap()));
	      this.currentTile.setCurrentLocation(myLocation);
	      setTitle();
	   }
	   
	   /**
	    * Get the current Tile being displayed by this TileView.
	    * @return the current tile
	    */
	   public Tile getCurrentTile() {
	      return this.currentTile;
	   }
	   
	   /**
	    * Should the tile's correct location be displayed?
	    * @param visible true if the title should be displayed
	    */
	   public void setNumbersVisible(boolean visible) {
	      this.numbersVisible = visible;
	      setTitle();
	   }
	   
	   /* (non-Javadoc)
	    * Set the title to row-column.  For display purposes, the indexes for
	    * row and column start at 1 instead of 0.
	    */
	   private void setTitle() {
	      title = currentTile.getCorrectLocation().toString();
	      if (numbersVisible) {
	         super.setTextColor(Color.RED);
	      } else {
	         super.setTextColor(Color.TRANSPARENT);
	      }
	      super.setText(title);
	   }
	}