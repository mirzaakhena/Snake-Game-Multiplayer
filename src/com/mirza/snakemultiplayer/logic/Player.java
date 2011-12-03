package com.mirza.snakemultiplayer.logic;

import java.awt.Color;
import java.io.Serializable;
import java.net.InetAddress;

public class Player implements Serializable {

	private static final long serialVersionUID = 1L;
	private int id;
	private String name;
	private InetAddress ipAddress;
	private Color color;

	public Player(int id, String name, InetAddress ipAddress, Color color) {
		this.id = id;
		this.name = name;
		this.ipAddress = ipAddress;
		this.color = color;
	}
	
	public Player(String name) {
		this.id = -1;
		this.name = name;
		this.ipAddress = null;
		this.color = null;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public InetAddress getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(InetAddress ipAddress) {
		this.ipAddress = ipAddress;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

}
