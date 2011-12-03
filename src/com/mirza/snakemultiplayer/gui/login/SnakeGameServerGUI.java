package com.mirza.snakemultiplayer.gui.login;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.DisplayMode;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GraphicsDevice;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.Timer;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import com.mirza.snakemultiplayer.gui.MainWindow;
import com.mirza.snakemultiplayer.logic.Direction;
import com.mirza.snakemultiplayer.logic.Player;
import com.mirza.snakemultiplayer.logic.Position;
import com.mirza.snakemultiplayer.logic.Snake;
import com.mirza.snakemultiplayer.logic.SnakeGameState;
import com.mirza.snakemultiplayer.network.ObjectListener;
import com.mirza.snakemultiplayer.network.UDPClientServer;

public class SnakeGameServerGUI extends JFrame implements ObjectListener, ActionListener {

	private static final long serialVersionUID = 1L;
	private JTable table;
	private DefaultTableModel tableModel;
	private JScrollPane scrollPane;
	private JLabel ipLabel;
	private JPanel statusPanel;
	private JButton buttonPlay;
	private JPanel panelButton;

	private SnakeGameState state;
	private UDPClientServer network;
	private ArrayList<Player> players;
	private Timer timer;
	private MainWindow mainWindow;

	private final int PORT = 55153;

	private Snake[] snakes;

	private final Color[] COLOR = new Color[] { Color.RED, Color.YELLOW, Color.GREEN, Color.BLUE, };
	private Snake[] SNAKE_CONSTANT = new Snake[] {
	//
			new Snake(10, Direction.RIGHT, new Position(0, 0)),//
			new Snake(10, Direction.DOWN, new Position(49, 0)),//
			new Snake(10, Direction.LEFT, new Position(49, 49)),//
			new Snake(10, Direction.UP, new Position(0, 49)),//
	};

	public SnakeGameServerGUI() {
		initialize();
		state = SnakeGameState.IDLE;
		network = new UDPClientServer();
		players = new ArrayList<Player>();
		timer = new Timer(100, this);
	}

	private void initialize() {
		String[] columnNames = new String[] { "Index", "Name", "IP", "Color", "Status", };
		tableModel = new DefaultTableModel(columnNames, 0);
		table = new JTable(tableModel);
		table.setIntercellSpacing(new Dimension(5, 5));
		table.getColumn("Color").setCellRenderer(new DefaultTableCellRenderer() {
			private static final long serialVersionUID = 1L;

			@Override
			protected void setValue(Object value) {
				if (value instanceof Color) {
					this.setBackground((Color) value);
				}
			}
		});

		scrollPane = new JScrollPane(table);
		this.add(scrollPane, BorderLayout.CENTER);

		try {
			ipLabel = new JLabel(" Server IP : " + getHostAddress().getHostAddress());
		} catch (UnknownHostException e) {
		}
		ipLabel.setFont(new Font("Georgia", Font.PLAIN, 24));
		statusPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 5));
		statusPanel.add(ipLabel);

		this.add(statusPanel, BorderLayout.NORTH);

		buttonPlay = new JButton("Start Server");
		buttonPlay.addActionListener(this);
		panelButton = new JPanel(new FlowLayout());
		panelButton.add(buttonPlay);
		this.add(panelButton, BorderLayout.SOUTH);

		GraphicsDevice gd = this.getGraphicsConfiguration().getDevice();
		DisplayMode display = gd.getDisplayMode();
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(600, 400);
		this.setLocation((display.getWidth() - this.getWidth()) / 2, (display.getHeight() - this.getHeight()) / 2);
		this.setTitle("Snake Game Multiplayer Server v1.5");
		this.setResizable(false);
		this.setVisible(true);
	}

	private InetAddress getHostAddress() throws UnknownHostException {
		return InetAddress.getLocalHost();
	}

	public static void main(String[] args) {
		new SnakeGameServerGUI();
	}

	/**
	 * 3.Server menerima pesan dari client, mendaftarkannya ke dalam list, lalu
	 * mengabarkan kepada client bahwa ia sudah terdaftar dengan membroadcast
	 * state server dan daftar para player lainnya serta nomor urut masing
	 * mereka.
	 */
	@Override
	public void receiveObject(Object obj, InetAddress a, int port) {

		Direction direction = null;
		int index = 0;

		switch (state) {
		case IDLE:
			// DO NOTHING...
			break;
		case COLLECT:
			Player p = (Player) obj;
			p.setId(players.size());
			p.setIpAddress(a);
			p.setColor(COLOR[players.size()]);
			players.add(p);
			tableModel.addRow(new Object[] { p.getId(), p.getName(), p.getIpAddress(), p.getColor(), "Ready" });
			if (players.size() == 4) {
				state = SnakeGameState.WAIT;
				buttonPlay.setText("Player Full!, Play Now");
			} else {
				buttonPlay.setText("Play Now With " + tableModel.getRowCount() + " Person");
			}
			for (Player player : players) {
				network.sendObject(new Object[] { state, players, player.getId() }, player.getIpAddress(), PORT);
			}
			break;
		case WAIT:
			direction = (Direction) ((Object[]) obj)[0];
			index = (Integer) ((Object[]) obj)[1];
			snakes[index].turn(direction);
			break;
		case PLAY:
			direction = (Direction) ((Object[]) obj)[0];
			index = (Integer) ((Object[]) obj)[1];
			snakes[index].turn(direction);
			break;
		case GAMEOVER:

			break;
		default:
			break;
		}
	}

	/**
	 * 1. Semua bermulai dari server yang dihidupkan. statenya berubah dari Idle
	 * menjadi Collect.Server skg akan mendengarkan player.
	 * 
	 * 6.suatu ketika ada saatnya server memutuskan untuk langsung bermain yang
	 * artinya ia akan mengubah statenya dan lalu membroadcast pesannya kepada
	 * semua player
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		switch (state) {
		case IDLE:
			state = SnakeGameState.COLLECT;
			this.setTitle(state.toString());
			network.startListening(PORT, this);
			buttonPlay.setText("being listening to the Player");
			break;
		case COLLECT:
			state = SnakeGameState.PLAY;
			snakes = new Snake[players.size()];
			for (int i = 0; i < players.size(); i++) {
				snakes[i] = SNAKE_CONSTANT[i];
			}
			for (Player player : players) {
				network.sendObject(new Object[] { state, snakes }, player.getIpAddress(), PORT);
			}
			try {
				mainWindow = new MainWindow(players, 0, getHostAddress(), network);
			} catch (UnknownHostException e1) {
			}
			timer.start();
			break;
		case WAIT:

			break;
		case PLAY:
			for (Snake s : snakes) {
				s.move();
			}
			for (Player player : players) {
				network.sendObject(new Object[] { snakes }, player.getIpAddress(), PORT);
			}
			mainWindow.receiveUpdate(snakes);
			break;
		case GAMEOVER:

			break;
		default:
			break;
		}
	}

	int xxx = 0;

}
