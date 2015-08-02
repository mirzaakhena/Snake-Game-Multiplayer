package com.mirza.snakemultiplayer.logic;

import java.io.Serializable;

public class Position implements Serializable {

	private static final long serialVersionUID = 6L;
	private int x;
	private int y;

	public Position(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public boolean equals(Position o) {
		return this.x == o.x && this.y == o.y;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	@Override
	public String toString() {
		return "(" + x + ", " + y + ")";
	}
}
