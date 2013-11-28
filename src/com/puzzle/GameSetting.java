package com.puzzle;

public class GameSetting {
	
	private int level;
	public int gridSizeWidth[] = new int[] {3, 3, 4, 4, 5};
	public int gridSizeHeight[] = new int[] {2, 3, 3, 4, 4};

	public GameSetting(int level) {
		this.level = level;
	}
	
	public int getWidth() {
		return gridSizeWidth[level];
	}
	
	public int getHeight() {
		return gridSizeHeight[level];
	}
}
