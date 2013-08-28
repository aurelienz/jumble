package com.morphoss.xo.jumble.models;

public enum Difficulty {
	EASY, MEDIUM, ADVANCED;

	public static Difficulty getDifficulty(int i) {
		if (i < 1)
			i = 1;
		if (i > 3)
			i = 3;
		return values()[i - 1];
	}
}