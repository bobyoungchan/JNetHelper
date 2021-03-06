package com.lhy.datahandle;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.sql.SQLException;

import javax.net.ssl.SSLSocket;

import com.lhy.datapipe.PipeHTC;
import com.lhy.ssl.SSLFactory;
import com.lhy.tools.Tools;

public class Socks5 implements Runnable {

	private InputStream ori_is;
	private OutputStream ori_os;
	private boolean isConn, isInDB;
	private int len;
	private String host;
	private int port;
	private int head = 5;
	private byte[] buffer = new byte[1024];

	public Socks5(InputStream ori_is, OutputStream ori_os) {
		this.ori_is = ori_is;
		this.ori_os = ori_os;
		isConn = isInDB = false;
	}

	@Override
	public void run() {
		getRequest();
		getHost();
		if (Tools.getAutoSwitch()) {
			autoSwitch();
		} else
			visitOutGFW();
	}

	private void autoSwitch() {
		try {
			if (Tools.getSql().executeQuery(Tools.isOutGFW(host)).next()) {
				isInDB = true;
				visitOutGFW();
			} else if (Tools.getSql().executeQuery(Tools.isInGFW(host)).next()) {
				isInDB = true;
				visitInGFW(Tools.getVisitInGFW());
			}
			if (!isConn) {
				visitInGFW(Tools.getTestInGFW());
				if (!isConn)
					visitOutGFW();
				if (isConn) {
					if (!isInDB) {
						if (!Tools.getArray().contains(host)) {
							Tools.getArray().add(host);
							Thread t = new Thread(new IsInGFW(host, port));
							t.setPriority(Thread.MAX_PRIORITY);
							t.start();
						}
					}
				} else
					erroPage();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void getHost() {
		host = new String(buffer, 5, len);
		port = findPort(buffer, 5 + len, 6 + len);
	}

	private void getRequest() {
		try {
			write(Tools.getAccepted5());
			for (int i = 0; i < head; i++) {
				buffer[i] = (byte) ori_is.read();
			}
			len = Integer.valueOf(buffer[4]);
			for (int i = 0; i < len + 2; i++) {
				buffer[5 + i] = (byte) ori_is.read();
			}
		} catch (IOException e) {
		}
	}

	private void visitOutGFW() {
		int retry = 5;
		while (retry > 0) {
			retry--;
			SSLFactory ssl = new SSLFactory();
			SSLSocket socket = ssl.getInstance();
			if (socket != null && socket.isConnected()) {
				isConn = true;
				Tools.setTa("Host:" + host + " Port:" + socket.getPort());
				Thread t = new Thread(new PipeHTC(ori_is, ori_os, socket,
						buffer, 7 + len, true));
				t.setPriority(Thread.MAX_PRIORITY);
				t.start();
				break;
			}
		}
	}

	private void visitInGFW(int time) {
		Socket socket = null;
		int retry = 5;
		try {
			while (retry > 0) {
				retry--;
				socket = new Socket();
				socket.connect(new InetSocketAddress(host, port), time);
				if (socket.isConnected() && socket != null) {
					isConn = true;
					Tools.setTa("Host:" + host + " Port:" + port);
					write(Tools.getConnectOK());
					Thread t = new Thread(new PipeHTC(ori_is, ori_os, socket,
							false));
					t.setPriority(Thread.MAX_PRIORITY);
					t.start();
					break;
				}
			}
		} catch (IOException e) {
			try {
				socket.close();
			} catch (IOException e1) {
			}
		}
	}

	private void write(byte[] bt) {
		try {
			ori_os.write(bt);
			ori_os.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void erroPage() {
		try {
			write(Tools.getConnectOK());
			ori_is.read(new byte[1024]);
			ori_os.write("<html><center><BR><BR><BR><BR><BR><B>(:&nbsp&nbsp&nbspThanks for you use JNetHelper, but you should connect to the Internet first.&nbsp&nbsp&nbsp:)<BR><BR>-----JNetHelper Version Beta 1.1 </B></center></html>"
					.getBytes());
			ori_os.close();
			ori_is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private int findPort(byte[] bArray, int begin, int end) {
		int port = 0;
		port |= Integer.valueOf(0xFF & bArray[begin]);
		port <<= 8;
		port |= Integer.valueOf(0xFF & bArray[end]);
		return port;
	}
}
