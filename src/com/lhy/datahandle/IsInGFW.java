package com.lhy.datahandle;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.sql.SQLException;

import com.lhy.tools.Tools;

public class IsInGFW implements Runnable {

	private String host;
	private int port;
	private int times;
	private int connected, disconnected;

	public IsInGFW(String host, String port) {
		times = 9;
		this.host = host;
		this.port = Integer.parseInt(port);
		connected = disconnected = 0;
	}

	@Override
	public void run() {
		while (times > 0) {
			try {
				times--;
				Socket tar_socket = new Socket();
				tar_socket.connect(new InetSocketAddress(host, port),
						Tools.ISINGFW);
				connected++;
				tar_socket.close();
			} catch (IOException e) {
				disconnected++;
			}
		}
		dbOpr();
	}

	private void dbOpr() {
		try {
			if (disconnected < connected)
				Tools.getSql().execute(Tools.putInGFW(host));
			else
				Tools.getSql().execute(Tools.putOutGFW(host));
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
