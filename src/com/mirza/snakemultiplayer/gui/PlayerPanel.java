package com.mirza.snakemultiplayer.gui;

import java.awt.Color;
import java.awt.Font;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class PlayerPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private JLabel labelName;
	private JLabel labelIP;
	private JLabel labelScore;

	public PlayerPanel(Color color) {
		this.setBackground(color);
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		labelName = new JLabel("Mirza Akhena");
		labelIP = new JLabel("192.168.111.222");
		labelScore = new JLabel("392");

		labelName.setFont(new Font("Georgia", Font.PLAIN, 32));
		labelIP.setFont(new Font("Georgia", Font.PLAIN, 30));
		labelScore.setFont(new Font("Georgia", Font.PLAIN, 80));

		this.add(labelName);
		this.add(labelIP);
		this.add(labelScore);
	}

	public void setScore(int i) {
		labelScore.setText(String.valueOf(i));
	}
}
