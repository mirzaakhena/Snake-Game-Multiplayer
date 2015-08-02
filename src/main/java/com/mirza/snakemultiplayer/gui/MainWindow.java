package com.mirza.snakemultiplayer.gui;

import java.awt.Color;
import java.awt.DisplayMode;
import java.awt.Graphics;
import java.awt.GraphicsDevice;
import java.awt.event.KeyEvent;
import java.net.InetAddress;
import java.util.ArrayList;

import javax.swing.JFrame;
import com.mirza.snakemultiplayer.logic.Direction;
import com.mirza.snakemultiplayer.logic.Field;
import com.mirza.snakemultiplayer.logic.Player;
import com.mirza.snakemultiplayer.logic.Position;
import com.mirza.snakemultiplayer.logic.Snake;
import com.mirza.snakemultiplayer.network.UDPClientServer;

public class MainWindow extends MyWindowAdapter {

	private static final long serialVersionUID = 9L;

	private static final int PORT = 55153;

	private Screen screen;
	private PlayerPanel[] panel;
	private Field field;
	private int blockRange;

	private ArrayList<Player> players;
	private int indexOnServer;
	private InetAddress serverAddress;

	private UDPClientServer network;
	private Snake[] snakes;

	public MainWindow(ArrayList<Player> players, int indexOnServer, InetAddress server, UDPClientServer network) {
		this.players = players;
		this.indexOnServer = indexOnServer;
		field = new Field(50, 50);
		serverAddress = server;
		this.network = network;
		snakes = null;
		initialize();
	}

	private void initialize() {
		this.setUndecorated(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLayout(null);
		this.setFullScreen();

		blockRange = (int) (this.getHeight() * 0.99) / field.getHeight();
		int posx = (this.getWidth() - field.getWidth() * blockRange) / 2;
		int posy = (this.getHeight() - field.getHeight() * blockRange) / 2;
		screen = new Screen(this);
		screen.setBounds(posx, posy, field.getWidth() * blockRange, field.getHeight() * blockRange);
		this.add(screen);

		panel = new PlayerPanel[4];

		int w = (int) (posx * 0.9);
		int x0 = (posx - w) / 2;
		int y0 = (this.getHeight() - screen.getHeight()) / 2;
		int h = (screen.getHeight() / 2) - y0;
		panel[0] = new PlayerPanel(Color.RED);
		panel[0].setBounds(x0, y0, w, h);
		this.getContentPane().add(panel[0]);

		int x1 = posx + screen.getWidth() + (posx - w) / 2;
		int y1 = y0;
		panel[1] = new PlayerPanel(Color.YELLOW);
		panel[1].setBounds(x1, y1, w, h);
		this.getContentPane().add(panel[1]);

		int x2 = x0;
		int y2 = h + 3 * y0;
		panel[2] = new PlayerPanel(Color.GREEN);
		panel[2].setBounds(x2, y2, w, h);
		this.getContentPane().add(panel[2]);

		int x3 = x1;
		int y3 = y2;
		panel[3] = new PlayerPanel(Color.BLUE);
		panel[3].setBounds(x3, y3, w, h);
		this.getContentPane().add(panel[3]);
		this.setVisible(true);
		screen.setDoubleBufferingEnabled();
	}

	public static void main(String[] args) {
		new MainWindow(null, 0, null, null);
	}

	private void setFullScreen() {
		GraphicsDevice gd = this.getGraphicsConfiguration().getDevice();
		DisplayMode display = gd.getDisplayMode();
		this.setSize(display.getWidth(), display.getHeight());
	}

	@Override
	public void keyPressed(KeyEvent key) {
		switch (key.getKeyCode()) {
		case KeyEvent.VK_UP:
			network.sendObject(new Object[] { Direction.UP, indexOnServer }, serverAddress, PORT);
			break;
		case KeyEvent.VK_DOWN:
			network.sendObject(new Object[] { Direction.DOWN, indexOnServer }, serverAddress, PORT);
			break;
		case KeyEvent.VK_LEFT:
			network.sendObject(new Object[] { Direction.LEFT, indexOnServer }, serverAddress, PORT);
			break;
		case KeyEvent.VK_RIGHT:
			network.sendObject(new Object[] { Direction.RIGHT, indexOnServer }, serverAddress, PORT);
			break;
		}
	}

	@Override
	public void draw(Graphics g) {
		screen.clearScreen();
		drawGrid(g);
		drawSnake(g);
	}

	private void drawSnake(Graphics g) {
		for (int k = 0; k < snakes.length; k++) {
			for (int i = 0; i < 2; i++) {
				g.setColor(players.get(k).getColor());
				int n = snakes[k].getNumberOfBody();
				for (int j = 0; j < n; j++) {
					Position p = snakes[k].getBodyPosition(j);
					g.fillRect(2 + p.getX() * blockRange, 2 + p.getY() * blockRange, blockRange - 3, blockRange - 3);
				}
			}
		}
	}

	private void drawGrid(Graphics g) {
		g.setColor(new Color(0xff222222));
		for (int i = 0; i < field.getWidth(); i++) {
			g.drawLine(blockRange * i, 0, blockRange * i, blockRange * field.getHeight());
		}
		for (int i = 0; i < field.getHeight(); i++) {
			g.drawLine(0, blockRange * i, blockRange * field.getWidth(), blockRange * i);
		}
	}

	public void receiveUpdate(Snake[] snakes) {
		this.snakes = snakes;
		screen.reDraw();
	}
}
