package com.lhy.datapipe;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class PipeHTCInGFW implements Runnable {

	String head;
	String changedheader;
	OutputStream ori_os, tar_os;
	InputStream ori_is, tar_is;
	Socket tar_socket;

	public PipeHTCInGFW(String head, String changedheader, InputStream ori_is,
			OutputStream ori_os, Socket tar_socket) {
		this.head = head;
		this.changedheader = changedheader;
		this.ori_os = ori_os;
		this.ori_is = ori_is;
		this.tar_socket = tar_socket;
	}

	@Override
	public void run() {
		try {
			tar_os = tar_socket.getOutputStream();
			tar_is = tar_socket.getInputStream();
			startCTH();
			startHTC();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void startHTC() {
		int index = 0;
		byte[] bt = new byte[1024];
		while (true) {
			try {
				if ((index = tar_is.read(bt, 0, bt.length)) > 0) {
					ori_os.write(bt, 0, index);
					if (tar_is.available() < 0)
						break;
				} else if (index <= 0) {
					break;
				}
			} catch (IOException e) {
				break;
			}
		}
		try {
			ori_os.close();
			tar_is.close();
		} catch (IOException e) {
		}
	}

	private void startCTH() {
		try {
			if (head.equalsIgnoreCase("CONNECT")) {
				ori_os.write((("HTTP/1.1 200 OK\r\n\r\n")).getBytes());
				startHTTPS();
			} else {
				tar_os.write(changedheader.getBytes());
				startHTTP();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void startHTTP() {
		try {
			Thread t = new Thread(new PipeCHTInGFW(ori_is,
					tar_socket.getOutputStream(), false));
			t.setPriority(Thread.MAX_PRIORITY);
			t.start();
		} catch (IOException e) {
			try {
				tar_socket.close();
				ori_is.close();
				ori_os.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
	}

	private void startHTTPS() {
		try {
			Thread t = new Thread(new PipeCHTInGFW(ori_is,
					tar_socket.getOutputStream(), true));
			t.setPriority(Thread.MAX_PRIORITY);
			t.start();
		} catch (IOException e) {
			try {
				tar_socket.close();
				ori_is.close();
				ori_os.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
	}

}
