package com.lhy.datahandle;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

import javax.net.ssl.SSLSocket;

import com.lhy.datapipe.PipeHTCInGFW;
import com.lhy.ssl.SSLFactory;
import com.lhy.tools.Tools;

public class Socks5 implements Runnable {

	InputStream ori_is;
	OutputStream ori_os;
	boolean isConn;
	private int len;
	private int head = 5;
	private byte[] buffer = new byte[1024];

	public Socks5(InputStream ori_is, OutputStream ori_os) {
		this.ori_is = ori_is;
		this.ori_os = ori_os;
		isConn = false;
	}

	@Override
	public void run() {
		getRequest();
		visitInGFW();
		if (!isConn)
			visitOutGFW();
	}

	private void getRequest() {
		try {
			write(Tools.getAccept());
			for (int i = 0; i < head; i++) {
				buffer[i] = (byte) ori_is.read();
			}
			len = Integer.valueOf(buffer[4]);
			for (int i = 0; i < len + 2; i++) {
				buffer[5 + i] = (byte) ori_is.read();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private byte[] Request(int len) {
		byte[] bt = new byte[7 + len];
		for (int i = 0; i < bt.length; i++)
			bt[i] = buffer[i];
		return bt;
	}

	private void visitOutGFW() {
		int retry = 5;
		while (retry > 0) {
			retry--;
			SSLFactory ssl = new SSLFactory();
			SSLSocket socket = ssl.getInstance();
			if (socket != null) {
				new Thread(new PipeHTCInGFW(ori_is, ori_os, socket,
						Request(len), true)).start();
				break;
			}
		}
	}

	public void visitInGFW() {
		int port = findPort(buffer, 5 + len, 6 + len);
		String ip = new String(buffer, 5, len);
		Socket socket = null;
		int retry = 5;
		try {
			while (retry > 0) {
				retry--;
				socket = new Socket();
				socket.connect(new InetSocketAddress(ip, port),
						Tools.getTestInGFW());
				if (socket.isConnected() && socket != null) {
					isConn = true;
					write(Tools.getConnectOK());
					new Thread(new PipeHTCInGFW(ori_is, ori_os, socket, false))
							.start();
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

	private int findPort(byte[] bArray, int begin, int end) {
		int port = 0;
		port |= Integer.valueOf(0xFF & bArray[begin]);
		port <<= 8;
		port |= Integer.valueOf(0xFF & bArray[end]);
		return port;
	}
}
