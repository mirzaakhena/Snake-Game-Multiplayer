package com.mirza.snakemultiplayer.logic;

import java.io.Serializable;

public class Field implements Serializable  {

	private static final long serialVersionUID = 4145319973034765292L;
	private int width;
	private int height;

	public Field(int width, int height) {
		this.width = width;
		this.height = height;
	}

	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}

}
