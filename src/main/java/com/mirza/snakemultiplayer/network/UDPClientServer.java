package com.mirza.snakemultiplayer.network;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UDPClientServer {

	private boolean isListening;
	private DatagramSocket socketToListen;

	public UDPClientServer() {
		isListening = false;
	}

	public void sendObject(Object obj, InetAddress address, int port) {
		try {
			byte[] data = ObjectBytes.objectToBytes(obj);
			DatagramPacket packet = new DatagramPacket(data, data.length, address, port);
			DatagramSocket socket = new DatagramSocket();
			socket.send(packet);
			socket.close();
		
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void startListening(final int port, final ObjectListener listener) {
		if (isListening == false) {

			new Thread() {

				@Override
				public void run() {
					byte[] buffer = new byte[1024 * 64];

					isListening = true;
					try {
						socketToListen = new DatagramSocket(port);
						System.out.println("Server start");
						while (true) {
							DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
							socketToListen.receive(packet);
							Object o = ObjectBytes.bytesToObject(packet.getData());
							InetAddress a = packet.getAddress();
							int p = packet.getPort();
							listener.receiveObject(o, a, p);
						}
					
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

			}.start();
		}
	}

	public void stopListening() {
		if (isListening == true) {
			new Thread() {
				@Override
				public void run() {
					Thread.interrupted();
					socketToListen.close();
					isListening = false;
				}
			}.start();
		}
	}
}
