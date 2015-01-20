package com.lhy.localmonitor;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import com.lhy.datahandle.NewRequest;
import com.lhy.tools.Tools;

public class MonitorWorker implements Runnable {

	private ServerSocket server;
	private Thread t;
	private byte[] bt = new byte[1024];

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
					if (Tools.getConnected()) {
						start(socket);
					} else {
						socket.getInputStream().read(bt, 0, bt.length);
						socket.getOutputStream().write(Tools.getAccepted5());
						socket.getInputStream().read(bt, 0, bt.length);
						socket.getOutputStream().write(Tools.getConnectOK());
						socket.getInputStream().read(bt, 0, bt.length);
						socket.getOutputStream()
								.write("<html><center><BR><BR><BR><BR><BR><B>(:&nbsp&nbsp&nbspThanks for you use JNetHelper, but you should connect to the Internet first.&nbsp&nbsp&nbsp:)<BR><BR>-----JNetHelper Version Beta 1.0 </B></center></html>"
										.getBytes());
						socket.close();
					}
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
