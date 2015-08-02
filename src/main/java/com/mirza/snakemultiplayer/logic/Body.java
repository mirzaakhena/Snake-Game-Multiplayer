package com.mirza.snakemultiplayer.logic;

import java.io.Serializable;

public class Body implements Serializable {

	private static final long serialVersionUID = 8L;
	private Direction direction;
	private Position position;

	public Body(Direction d, Position p) {
		this.direction = d;
		this.position = p;
	}

	public Direction getDirection() {
		return direction;
	}

	public Position getPosition() {
		return position;
	}

}
