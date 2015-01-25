package com.lhy.localmonitor;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import com.lhy.datahandle.NewRequest;
import com.lhy.tools.Tools;

public class MonitorWorker implements Runnable {

	private ServerSocket server;
	private Thread t;

	public MonitorWorker(ServerSocket server) {
		this.server = server;
	}

	@Override
	public void run() {
		Tools.setTa("程序初始化完毕！");
		while (true) {
			while (Tools.getServerMonitorFlag()) {
				try {
					Socket socket = server.accept();
					start(socket);
				} catch (IOException e) {
				}
			}
			Tools.sleep(2000);
		}
	}

	private void start(Socket socket) {
		t = new Thread(new NewRequest(socket));
		t.setPriority(Thread.MAX_PRIORITY);
		t.start();
	}

}
