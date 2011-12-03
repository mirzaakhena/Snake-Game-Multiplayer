package com.mirza.snakemultiplayer.network;

import java.net.InetAddress;

public interface ObjectListener {

	public void receiveObject(Object obj, InetAddress address, int port);

}
