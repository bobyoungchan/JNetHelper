package com.lhy.datahandle;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

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
	SSLFactory ssl;
	String[] header;
	String all = "";

	public NewRequest(Socket socket) {
		this.socket = socket;
	}

	@Override
	public void run() {
		try {
			ssl = new SSLFactory();
			ori_is = socket.getInputStream();
			ori_os = socket.getOutputStream();
			get_request_header();
			header = handle.getChangedHeader(all);
			visitInGFW();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void visitInGFW() {
		if (!header[0].equals("") && header[0] != null) {
			System.out.println(header[0] + "  " + Integer.parseInt(header[1]));
			int retry = 5;
//			visitInGFW(retry);
			visitOutGFW(retry);
		}
	}

	private void visitInGFW(int retry) {
		try {
			while (retry > 0) {
				retry--;
				tar_socket = new Socket();
				tar_socket.connect(
						new InetSocketAddress(header[0], Integer
								.parseInt(header[1])), Tools.TIMEOUTINGFW);
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
				break;
			}
			tar_os = tar_sslsocket.getOutputStream();
			tar_is = tar_sslsocket.getInputStream();
			Thread cth = new Thread(new PipeCTHOutGFW(all, ori_is, tar_os));
			Thread htc = new Thread(new PipeHTCOutGFW(tar_is, ori_os));
			cth.setPriority(Thread.MAX_PRIORITY);
			htc.setPriority(Thread.MAX_PRIORITY);
			cth.start();
			htc.start();
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
