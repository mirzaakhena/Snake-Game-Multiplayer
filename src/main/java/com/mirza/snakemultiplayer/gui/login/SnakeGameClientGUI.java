package com.mirza.snakemultiplayer.gui.login;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.DisplayMode;
import java.awt.FlowLayout;
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
import javax.swing.JTextField;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import com.mirza.snakemultiplayer.gui.MainWindow;
import com.mirza.snakemultiplayer.logic.Player;
import com.mirza.snakemultiplayer.logic.Snake;
import com.mirza.snakemultiplayer.logic.SnakeGameState;
import com.mirza.snakemultiplayer.network.ObjectListener;
import com.mirza.snakemultiplayer.network.UDPClientServer;

public class SnakeGameClientGUI extends JFrame implements ObjectListener, ActionListener {

	private static final long serialVersionUID = 1L;
	private JTable table;
	private DefaultTableModel tableModel;
	private JScrollPane scrollPane;

	private JLabel ipLabel;
	private JTextField ipField;
	private JLabel nameLabel;
	private JTextField nameField;
	private JPanel statusPanel;

	private JButton buttonPlay;
	private JPanel panelButton;

	private SnakeGameState state;
	private UDPClientServer network;
	private ArrayList<Player> players;
	private int indexOnServer;
	private MainWindow mainWindow;
	private Snake[] snakes;

	private final int PORT = 55153;

	public SnakeGameClientGUI() {
		initialize();
		state = SnakeGameState.IDLE;
		network = new UDPClientServer();
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

		ipLabel = new JLabel("Server IP : ");

		ipField = new JTextField(10);
		ipField.setText(getSubnet() + "113");
		ipField.setCaretPosition(ipField.getText().length());
		nameLabel = new JLabel("Name : ");
		nameField = new JTextField(15);
		nameField.setText(System.getProperty("user.name"));
		statusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 30, 10));
		statusPanel.add(ipLabel);
		statusPanel.add(ipField);
		statusPanel.add(nameLabel);
		statusPanel.add(nameField);
		this.add(statusPanel, BorderLayout.NORTH);

		buttonPlay = new JButton("Connect to Server");
		panelButton = new JPanel(new FlowLayout());
		panelButton.add(buttonPlay);
		buttonPlay.addActionListener(this);
		this.add(panelButton, BorderLayout.SOUTH);

		GraphicsDevice gd = this.getGraphicsConfiguration().getDevice();
		DisplayMode display = gd.getDisplayMode();
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(600, 400);
		this.setLocation((display.getWidth() - this.getWidth()) / 2, (display.getHeight() - this.getHeight()) / 2);
		this.setTitle("Snake Game Multiplayer Client v1.5");
		this.setResizable(false);
		this.setVisible(true);
	}

	public static void main(String[] args) {
		new SnakeGameClientGUI();
	}

	/**
	 * 4.Client lalu menerima paket dari server ,mendaftarkan para player
	 * lainnya, mengubah statenya, dan . kontrol pada client juga difreeze.
	 * 5.ketika player lain mendaftar, player juga akan mengupdate list player
	 * dan state.
	 */
	@Override
	@SuppressWarnings("unchecked")
	public void receiveObject(Object obj, InetAddress a, int port) {
		switch (state) {
		case IDLE:
			state = (SnakeGameState) ((Object[]) obj)[0];
			this.setTitle(state.toString());
			players = (ArrayList<Player>) ((Object[]) obj)[1];
			indexOnServer = (Integer) ((Object[]) obj)[2];
			updateTable();
			nameField.setEditable(false);
			ipField.setEditable(false);
			buttonPlay.setEnabled(false);
			break;
		case COLLECT:
			state = (SnakeGameState) ((Object[]) obj)[0];
			if (state == SnakeGameState.PLAY) {
				snakes = (Snake[]) ((Object[]) obj)[1];
				mainWindow = new MainWindow(players, indexOnServer, getServerAddress(), network);
			} else {
				players = (ArrayList<Player>) ((Object[]) obj)[1];
				indexOnServer = (Integer) ((Object[]) obj)[2];
				updateTable();
			}
			break;
		case WAIT:

			break;
		case PLAY:
			snakes = (Snake[]) ((Object[]) obj)[0];
			mainWindow.receiveUpdate(snakes);
			break;
		case GAMEOVER:

			break;
		default:
			break;
		}
	}

	private void updateTable() {
		int n = tableModel.getRowCount();
		for (int i = 0; i < n; i++) {
			tableModel.removeRow(0);
		}
		for (Player p : players) {
			tableModel.addRow(new Object[] { p.getId(), p.getName(), p.getIpAddress(), p.getColor(), state.toString() });
		}
	}

	/**
	 * 2.Player mendaftarkan diri ke server dengan mengirim namanya saja.dan
	 * juga menghidupkan listenernya.
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		switch (state) {
		case IDLE:
			network.startListening(PORT, this);
			network.sendObject(new Player(nameField.getText()), getServerAddress(), PORT);
			break;
		case COLLECT:

			break;
		case WAIT:

			break;
		case PLAY:

			break;
		case GAMEOVER:

			break;
		default:
			break;
		}
	}

	private static InetAddress getHostAddress(String ip) {
		try {
			return InetAddress.getByName(ip);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return null;
	}

	private static String getSubnet() {
		String s = "";
		try {
			s = InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		int n = s.length();
		for (int i = 0; i < n; i++) {
			if (s.charAt(n - i - 1) == '.') {
				s = s.substring(0, n - i);
				break;
			}
		}
		return s;
	}

	public InetAddress getServerAddress() {
		return getHostAddress(ipField.getText());
	}
}
