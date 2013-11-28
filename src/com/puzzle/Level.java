/**
 * Class level. Stores the current level of the game and returns the number
 * of tiles to be used in the puzzle
 */
package com.puzzle;

import java.io.Serializable;

/**
 * @author Adam
 * 
 */
public class Level implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int level;
	/*
	 * Array of values to determine the award system
	 */
	public int[] CLASSIC_1 = { 18, 22, 26 };
	public int[] CLASSIC_2 = { 22, 26, 30 };
	public int[] CLASSIC_3 = { 22, 26, 30 };
	public int[] CLASSIC_4 = { 22, 26, 30 };
	public int[] CLASSIC_5 = { 36, 42, 54 };
	/*
	 * top socres for the levels
	 */
	public Integer classicScore_1 = 0;
	public Integer classicScore_2 = 0;
	public Integer classicScore_3 = 0;
	public Integer classicScore_4 = 0;
	public Integer classicScore_5 = 0;

	// private double[] levelTime;

	/**
	 * Default constructor, initialises level to 0
	 */
	public Level() {
		this(-1);
	}

	/**
	 * Constructor - takes integer value to initialise level.
	 * 
	 * @param Level
	 *            - the level of the game
	 */
	public Level(int position) {
		this.level = position;
	}

	//returns the score depending on the level being played
	public Integer getScore(int level) {
		Integer score = 0;
		switch (level) {
		case 0: {
			score = classicScore_1;
			break;
		}
		case 1: {
			score = classicScore_2;
			break;
		}
		case 2: {
			score = classicScore_3;
			break;
		}
		case 3: {
			score = classicScore_4;
			break;
		}
		case 4: {
			score = classicScore_5;
			break;
		}
		}
		return score;
	}

	/**
	 * Takes the level selected and returns the score for that selected level
	 * 
	 * @param level
	 *            - Takes the selected level
	 * @return - Score for the selected level
	 */
	public int[] setScore(int level, Integer score) {
		int[] returnArray = null;
		switch (level) {
		case 0: {
			if (classicScore_1 == 0 || score < this.classicScore_1)
				classicScore_1 = score;
			returnArray = CLASSIC_1;
			break;
		}
		case 1: {
			if (classicScore_2 == 0 || score < this.classicScore_2)
				classicScore_2 = score;
			returnArray = CLASSIC_2;
			break;
		}
		case 2: {
			if (classicScore_3 == 0 || score < this.classicScore_3)
				classicScore_3 = score;
			returnArray = CLASSIC_3;
			break;
		}
		case 3: {
			if (classicScore_4 == 0 || score < this.classicScore_4)
				classicScore_4 = score;
			returnArray = CLASSIC_4;
			break;
		}
		case 4: {
			if (classicScore_5 == 0 || score < this.classicScore_5)
				classicScore_5 = score;
			returnArray = CLASSIC_5;
			break;
		}
		}
		return returnArray;
	}

	/**
	 * Retrieves the level of the game
	 * 
	 * @return - Integer value game level
	 */
	public int getLevel() {
		return level;
	}

	/**
	 * Sets the current level of the game
	 * 
	 * @param level
	 *            - Takes integer for level
	 */
	public void setLevel(int level) {
		this.level = level;

	}

}
