package com.mirza.snakemultiplayer.logic;

import java.util.Random;


public class Food {

	private Position position;
	private Random random;

	public Food() {
		random = new Random();
	}

	public void randomize(int w, int h) {
		int x = random.nextInt(w);
		int y = random.nextInt(h);
		position = new Position(x, y);
	}

	public Position getPosition() {
		return position;
	}

}
