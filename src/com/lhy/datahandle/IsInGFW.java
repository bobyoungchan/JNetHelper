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

	public IsInGFW(String host, int port) {
		times = 9;
		this.host = host;
		this.port = port;
		connected = disconnected = 0;
	}

	@Override
	public void run() {
		while (times > 0) {
			try {
				times--;
				Socket tar_socket = new Socket();
				tar_socket.connect(new InetSocketAddress(host, port),
						Tools.getIsInGFW());
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
			if (disconnected < connected) {
				if (!Tools.getSql().executeQuery(Tools.isInGFW(host)).next()
						&& !Tools.getSql().executeQuery(Tools.isOutGFW(host))
								.next())
					Tools.getSql().execute(Tools.putInGFW(host));
			} else if (!Tools.getSql().executeQuery(Tools.isInGFW(host)).next()
					&& !Tools.getSql().executeQuery(Tools.isOutGFW(host))
							.next())
				Tools.getSql().execute(Tools.putOutGFW(host));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		Tools.getArray().remove(host);
		System.out.println("已移除 " + Tools.getArray().size());
	}
}
