package com.puzzle;

import android.graphics.Bitmap;

public class Tile {
	   
	   private TileLocation currentLocation; // current spot on game board
	   private final TileLocation correctLocation; // where it's trying to go
	   private Bitmap bitmap; // the partial picture that when combined with
	                         // the other Tiles will create the original
	                                // picture
	   
	   /**
	    * @param bitmap the partial picture
	    * @param correctRow objective row (0-based index)
	    * @param correctColumn objective column (0-based index)
	    */
	   public Tile(Bitmap bitmap, int correctRow, int correctColumn) {
	      this.bitmap = bitmap;
	      currentLocation = new TileLocation(correctRow, correctColumn);
	      correctLocation = new TileLocation(correctRow, correctColumn);
	   }
	   
	   /**
	    * Accessor method for the tile's current location.
	    * @return the current location of the tile on the game board
	    */
	   public TileLocation getCurrentLocation() {
	      return currentLocation;
	   }
	   
	   /**
	    * Accessor method for the tile's final location.
	    * @return the final locaiton of the tile on the game board
	    */
	   public TileLocation getCorrectLocation() {
	      return correctLocation;
	   }
	   
	   /**
	    * Is this tile in the location such that it would correctly display the
	    * image if combined with the other tiles in their correct locations?
	    * @return true if the tile is in its correct location
	    */
	   public boolean isCorrect() {
	      return currentLocation.equals(correctLocation);
	   }
	   
	   /**
	    * The tile has been moved, and its location should be updated.
	    * @param location the new current location of the tile
	    */
	   public void setCurrentLocation(TileLocation location) {
	      this.currentLocation = location;
	   }
	   
	   /**
	    * The tile has been moved, and its location should be updated.
	    * @param row the row number (0-based index) of the current location
	    * @param column the column number (0-based index) of the current location
	    */
	   public void setCurrentLocation(int row, int column) {
	      this.currentLocation.setRow(row);
	      this.currentLocation.setColumn(column);
	   }
	   
	   /**
	    * The part of the picture that this tile contains
	    * @return the partial picture contained within this tile
	    */
	   public Bitmap getBitmap() {
	      return bitmap;
	   }
	   
	   public void setBitmap(Bitmap bitmap) {
		   this.bitmap = bitmap;
	   }
	   
	   
	   /**
	    * Clear the memory used by the bitmap.  This should only be called when
	    * the tile is no longer needed as future calls to getBitmap will return
	    * null.
	    */
	   public void freeBitmap() {
	      bitmap.recycle();
	      bitmap = null;
	   }
	}