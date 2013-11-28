package com.puzzle;

public class TileLocation {

	   
	   private int row;
	   private int column;
	   
	   public TileLocation(int row, int column) {
	      this.row = row;
	      this.column = column;
	   }
	   
	   public int getRow() {
	      return row;
	   }
	   public void setRow(int row) {
	      this.row = row;
	   }
	   public int getColumn() {
	      return column;
	   }
	   public void setColumn(int column) {
	      this.column = column;
	   }
	   
	   /**
	    * Do these tiles have the same row and column numbers?
	    * Checks to see if the location has already been assigned.
	    * @param other
	    * @return true if the row and column numbers are equal
	    */
	   public boolean equals(TileLocation other) {
	      return (other.getColumn() == this.getColumn()) && 
	            (other.getRow() == this.getRow());
	   }
	   
	   /**
	    * Are these tiles next to each other? Only considers tiles that are 
	    * left-right or up-down adjacent - no diagonals.
	    * @param other
	    * @return true if the tiles are adjacent
	    */
	   public boolean isAdjacent(TileLocation other) {
	      return ((other.getRow() == row && 
	               (other.getColumn() == column + 1 
	               || other.getColumn() == column - 1))
	            || (other.getColumn() == column &&
	               (other.getRow() == row + 1
	               || other.getRow() == row - 1)));
	   }   
	   
	   /**
	    * @return the current Yen-to-chicken exchange ratio.
	    */
	   public String toString(){
	      return new String((row + 1) + "-" + (column + 1));
	   }
	}