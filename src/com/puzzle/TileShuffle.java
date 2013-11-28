package com.puzzle;

public class TileShuffle {

	private int level;
	public int[] CLASSIC_1 = { 2, 1, 4, 3, 0, 1, 4, 5, 2, 1, 0, 3, 4, 1, 2, 5 };
	// solvable in 16 moves
	public int[] CLASSIC_2 = { 7, 4, 3, 0, 1, 4, 5, 2, 1, 4, 7, 6, 3, 4, 5, 2,
			1, 0, 3, 4, 7, 8 };
	// solvable in 22 moves
	public int[] CLASSIC_3 = { 7, 3, 2, 1, 5, 4, 0, 1, 5, 9, 10, 11, 7, 3, 2,
			6, 5, 4, 8, 9, 10, 11 };
	// solvable in 22 moves
	public int[] CLASSIC_4 = { 11, 10, 9, 13, 12, 8, 4, 5, 1, 0, 4, 8, 9, 5, 6,
			2, 3, 7, 11, 10, 14, 15 };
	// solvable in 22 moves
	public int[] CLASSIC_5 = { 18, 17, 12, 11, 16, 15, 10, 11, 6, 5, 0, 1, 6,
			11, 12, 13, 14, 9, 8, 3, 4, 9, 8, 13, 12, 11, 6, 7, 2, 3, 4, 9, 14,
			19 };
	//solvable in 34 moves

	public TileShuffle(int level) {
		this.level = level;
	}

	//sets the puzzle level
	public void setLevel(int level) {
		this.level = level;
	}

	//returns the puzzle level
	public int getLevel() {
		return level;
	}

	//returns an array to determine how the puzzle is shuffled
	public int[] getArray() {
		int[] returnArray = null;
		switch (level) {
		case 0: {
			returnArray = CLASSIC_1;
			break;
		}
		case 1: {
			returnArray = CLASSIC_2;
			break;
		}
		case 2: {
			returnArray = CLASSIC_3;
			break;
		}
		case 3: {
			returnArray = CLASSIC_4;
			break;
		}
		case 4: {
			returnArray = CLASSIC_5;
			break;
		}
		}
		return returnArray;
	}
}
