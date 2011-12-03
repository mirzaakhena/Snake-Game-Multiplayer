package com.mirza.snakemultiplayer.logic;

import java.io.Serializable;
import java.util.ArrayList;

public class Snake implements Serializable {

	private static final long serialVersionUID = 9L;
	private ArrayList<Body> bodies;
	private Direction direction;

	public Snake(int n, Direction d, Position p) {
		bodies = new ArrayList<Body>();
		bodies.add(new Body(d, p));
		AddNewBody(n - 1);
		direction = d;
	}

	public void turn(Direction d) {
		if (this.isPerpendicular(getHeadDirection(), d)) {
			this.direction = d;
		}
	}

	public void move() {
		if (direction == null) {
			move(getPositionFromDirection(getHeadDirection()));
		} else {
			bodies.remove(bodies.size() - 1);
			bodies.add(0, new Body(direction, getPositionFromDirection(direction)));
			direction = null;
		}
	}

	private void move(Position p) {
		bodies.remove(bodies.size() - 1);
		bodies.add(0, new Body(getHeadDirection(), p));
	}

	public void penetratedTheWall(Field field) {
		if (this.getHeadPosition().getX() >= field.getWidth()) {
			this.move(new Position(0, this.getHeadPosition().getY()));
		} else if (this.getHeadPosition().getY() >= field.getHeight()) {
			this.move(new Position(this.getHeadPosition().getX(), 0));
		} else if (this.getHeadPosition().getX() < 0) {
			this.move(new Position(field.getWidth() - 1, this.getHeadPosition().getY()));
		} else if (this.getHeadPosition().getY() < 0) {
			this.move(new Position(this.getHeadPosition().getX(), field.getHeight() - 1));
		}
	}

	private Position getPositionFromDirection(Direction d) {
		Position p = getHeadPosition();
		switch (d) {
		case DOWN:
			return new Position(p.getX(), p.getY() + 1);
		case LEFT:
			return new Position(p.getX() - 1, p.getY());
		case RIGHT:
			return new Position(p.getX() + 1, p.getY());
		case UP:
			return new Position(p.getX(), p.getY() - 1);
		}
		return null;
	}

	private boolean isPerpendicular(Direction a, Direction b) {
		boolean w = (a == Direction.DOWN || a == Direction.UP);
		boolean x = (b == Direction.LEFT || b == Direction.RIGHT);
		if (w && x) {
			return true;
		}
		boolean y = (a == Direction.LEFT || a == Direction.RIGHT);
		boolean z = (b == Direction.DOWN || b == Direction.UP);
		if (y && z) {
			return true;
		}
		return false;
	}

	public void AddNewBody() {
		AddNewBody(1);
	}

	public void AddNewBody(int n) {
		for (int i = 0; i < n; i++) {
			Direction d = getTailDirection();
			Position p = getTailPosition();
			switch (d) {
			case LEFT:
				bodies.add(new Body(d, new Position(p.getX() + 1, p.getY())));
				break;
			case RIGHT:
				bodies.add(new Body(d, new Position(p.getX() - 1, p.getY())));
				break;
			case UP:
				bodies.add(new Body(d, new Position(p.getX(), p.getY() + 1)));
				break;
			case DOWN:
				bodies.add(new Body(d, new Position(p.getX(), p.getY() - 1)));
				break;
			default:
				break;
			}
		}
	}

	public void removeBody(int n) {
		if (bodies.size() > 1) {
			for (int i = 0; i < n; i++) {
				bodies.remove(bodies.size() - 1);
			}
		}
	}

	public void removeBody() {
		removeBody(1);
	}

	public Position getHeadPosition() {
		return bodies.get(0).getPosition();
	}

	public Direction getHeadDirection() {
		return bodies.get(0).getDirection();
	}

	public Position getBodyPosition(int i) {
		return bodies.get(i).getPosition();
	}

	public Direction getBodyDirection(int i) {
		return bodies.get(i).getDirection();
	}

	public Position getTailPosition() {
		return bodies.get(bodies.size() - 1).getPosition();
	}

	public Direction getTailDirection() {
		return bodies.get(bodies.size() - 1).getDirection();
	}

	public int getNumberOfBody() {
		return bodies.size();
	}

	@Override
	public String toString() {
		String message = "";
		message += "Direction to " + direction.toString() + " and";
		message += "Head at " + getHeadPosition().toString();
		return message;
	}

}
