package com.lhy.datahandle;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

import com.lhy.datapipe.PipeHTCInGFW;
import com.lhy.tools.Tools;

public class NewRequest implements Runnable {

	private Socket socket, tar_socket;
	private InputStream is;
	private OutputStream os;
	private Thread t;
	String[] header;
	String all = "";

	public NewRequest(Socket socket) {
		this.socket = socket;
	}

	@Override
	public void run() {
		try {
			is = socket.getInputStream();
			os = socket.getOutputStream();
			get_request_header();
			header = Tools.getChangedHeader(all);
			visitInGFW();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void visitInGFW() {
		try {
			tar_socket = new Socket();
			System.out.println(header[0] + "  " + Integer
					.parseInt(header[1]));
			tar_socket.connect(
					new InetSocketAddress(header[0], Integer
							.parseInt(header[1])), Tools.TIMEOUTINGFW);
			t = new Thread(new PipeHTCInGFW(header[2],header[3],is,os,tar_socket));
			t.setPriority(Thread.MAX_PRIORITY);
			t.start();
		} catch (NumberFormatException e) {
			e.printStackTrace();
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
			while ((index = is.read(bt, 0, bt.length)) > 0) {
				all += new String(bt, 0, index);
				if (is.available() == 0)
					break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
