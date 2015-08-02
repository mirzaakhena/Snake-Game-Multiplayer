package com.mirza.snakemultiplayer.gui;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;

public abstract class MyWindowAdapter extends JFrame implements WindowListener, KeyListener, Drawable {

	private static final long serialVersionUID = -4769940185735413350L;

	public MyWindowAdapter() {
		this.addKeyListener(this);
		this.addWindowListener(this);
	}

	@Override
	public void windowActivated(WindowEvent e) {

	}

	@Override
	public void windowClosed(WindowEvent e) {
	}

	@Override
	public void windowClosing(WindowEvent e) {
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
	}

	@Override
	public void windowIconified(WindowEvent e) {
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void windowOpened(WindowEvent e) {

	}

	@Override
	public abstract void keyPressed(KeyEvent e);

	@Override
	public abstract void draw(Graphics g);

}
