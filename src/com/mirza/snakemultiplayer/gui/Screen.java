package com.mirza.snakemultiplayer.gui;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;

public class Screen extends Canvas {

	private static final long serialVersionUID = 3010909275982998929L;
	private BufferStrategy bufferStrategy;
	private Graphics graphics;
	private Drawable drawable;

	public Screen(Drawable drawable) {
		this.drawable = drawable;
		this.setBackground(Color.BLACK);
	}

	public void setDoubleBufferingEnabled() {
		this.createBufferStrategy(2);
		bufferStrategy = this.getBufferStrategy();
	}

	public void reDraw() {
		graphics = bufferStrategy.getDrawGraphics();
		drawable.draw(graphics);
		bufferStrategy.show();
		graphics.dispose();
	}
	
	public void clearScreen() {
		graphics.setColor(Color.BLACK);
		graphics.fillRect(0, 0, this.getWidth(), this.getHeight());
	}

}
