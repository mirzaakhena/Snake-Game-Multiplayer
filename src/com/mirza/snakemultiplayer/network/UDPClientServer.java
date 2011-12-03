package com.mirza.snakemultiplayer.network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

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
		} catch (SocketException e) {
			System.err.println(e);
		} catch (UnknownHostException e) {
			System.err.println(e);
		} catch (IOException e) {
			System.err.println(e);
		}
	}

	public void startListening(final int port, final ObjectListener listener) {
		if (isListening == false) {

			new Thread(new Runnable() {

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
					} catch (SocketException e) {
						System.err.println(e);
					} catch (IOException e) {
						System.err.println(e);
					} catch (ClassNotFoundException e) {
						System.err.println(e);
					}
				}

			}).start();
		}
	}

	public void stopListening() {
		if (isListening == true) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					Thread.interrupted();
					socketToListen.close();
					isListening = false;
				}
			}).start();
		}
	}
}
