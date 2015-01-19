package com.lhy.localmonitor;

import java.io.IOException;
import java.net.ServerSocket;

import com.lhy.tools.Tools;

public class MonitorInitialization {

	private ServerSocket server;
	private Thread t;

	public MonitorInitialization() {
		initialization();
		t.start();
	}

	private void initialization() {
		try {
			server = new ServerSocket(Tools.getLocalPort());
			t = new Thread(new MonitorWorker(server));
			t.setPriority(Thread.MAX_PRIORITY);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		new MonitorInitialization();
	}
}
