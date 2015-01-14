package com.lhy.datahandle;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.sql.SQLException;

import javax.net.ssl.SSLSocket;

import com.lhy.datapipe.PipeCTHOutGFW;
import com.lhy.datapipe.PipeHTCInGFW;
import com.lhy.datapipe.PipeHTCOutGFW;
import com.lhy.ssl.SSLFactory;
import com.lhy.tools.HeaderHandle;
import com.lhy.tools.Tools;

public class NewRequest implements Runnable {

	private HeaderHandle handle = new HeaderHandle();
	private Socket socket, tar_socket;
	private SSLSocket tar_sslsocket;
	private InputStream ori_is, tar_is;
	private OutputStream ori_os, tar_os;
	private Thread t;
	private SSLFactory ssl;
	private String[] header;
	private String all;
	private boolean isConn, isInDB;

	public NewRequest(Socket socket) {
		all = "";
		isConn = isInDB = false;
		this.socket = socket;
	}

	@Override
	public void run() {
		initialization();
		if (!header[0].equals("") && header[0] != null) {
			autoswitch();
		}
	}

	private void autoswitch() {
		try {
			if (Tools.getSql().executeQuery(Tools.isInGFW(header[0])).next()) {
				isInDB = true;
				System.out.println(header[0] + "墙内");
				visitInGFW(Tools.RETRY, Tools.VISITINGFW);
			} else if (Tools.getSql().executeQuery(Tools.isOutGFW(header[0]))
					.next()) {
				isInDB = true;
				System.out.println(header[0] + "墙外");
				visitOutGFW(Tools.RETRY);
			}
			if (!isInDB || !isConn) {
				System.out.println(header[0] + "无记录，即将测试");
				visitInGFW(Tools.RETRY, Tools.TESTINGFW);
				if (!isConn)
					visitOutGFW(Tools.RETRY);
				if (!Tools.getSql().executeQuery(Tools.isInGFW(header[0]))
						.next()
						&& !Tools.getSql()
								.executeQuery(Tools.isOutGFW(header[0])).next()) {
					Thread t = new Thread(new IsInGFW(header[0], header[1]));
					t.setPriority(Thread.MIN_PRIORITY);
					t.start();
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void initialization() {
		try {
			ssl = new SSLFactory();
			ori_is = socket.getInputStream();
			ori_os = socket.getOutputStream();
			get_request_header();
			header = handle.getChangedHeader(all);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void visitInGFW(int retry, int times) {
		try {
			while (retry > 0) {
				retry--;
				tar_socket = new Socket();
				tar_socket.connect(
						new InetSocketAddress(header[0], Integer
								.parseInt(header[1])), times);
				isConn = true;
				break;
			}
			if (tar_socket.isConnected() && tar_socket != null) {
				t = new Thread(new PipeHTCInGFW(header[2], header[3], ori_is,
						ori_os, tar_socket));
				t.setPriority(Thread.MAX_PRIORITY);
				t.start();
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
		}
	}

	private void visitOutGFW(int retry) {
		try {
			while (retry > 0) {
				retry--;
				tar_sslsocket = (SSLSocket) ssl.getInstance();
				isConn = true;
				break;
			}
			if (tar_sslsocket.isConnected() && tar_sslsocket != null) {
				tar_os = tar_sslsocket.getOutputStream();
				tar_is = tar_sslsocket.getInputStream();
				Thread cth = new Thread(new PipeCTHOutGFW(all, ori_is, tar_os));
				Thread htc = new Thread(new PipeHTCOutGFW(tar_is, ori_os));
				cth.setPriority(Thread.MAX_PRIORITY);
				htc.setPriority(Thread.MAX_PRIORITY);
				cth.start();
				htc.start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取完整的请求
	 */
	private void get_request_header() {
		int index;
		try {
			byte[] bt = new byte[1024];
			while ((index = ori_is.read(bt, 0, bt.length)) > 0) {
				all += new String(bt, 0, index);
				if (ori_is.available() == 0)
					break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
