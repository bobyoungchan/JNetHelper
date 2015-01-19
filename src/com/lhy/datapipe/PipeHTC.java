package com.lhy.datapipe;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import javax.net.ssl.SSLSocket;

public class PipeHTC implements Runnable {
	private OutputStream ori_os, tar_os;
	private InputStream ori_is, tar_is;
	private Socket tar_socket;
	private int request_len;
	private boolean isOut;
	private byte[] buffer;

	public PipeHTC(InputStream ori_is, OutputStream ori_os,
			Socket tar_socket, boolean isOut) {
		this.isOut = isOut;
		this.ori_os = ori_os;
		this.ori_is = ori_is;
		this.tar_socket = tar_socket;
	}

	public PipeHTC(InputStream ori_is, OutputStream ori_os,
			SSLSocket tar_socket, byte[] buffer, int request_len, boolean isOut) {
		this.isOut = isOut;
		this.ori_os = ori_os;
		this.ori_is = ori_is;
		this.buffer = buffer;
		this.tar_socket = tar_socket;
		this.request_len = request_len;
	}

	@Override
	public void run() {
		try {
			tar_os = tar_socket.getOutputStream();
			tar_is = tar_socket.getInputStream();
			if (isOut){
				tar_os.write(buffer, 0, request_len);
				tar_os.flush();
			}
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
					ori_os.flush();
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
		Thread t = new Thread(new PipeCTH(ori_is, tar_os));
		t.setPriority(Thread.MAX_PRIORITY);
		t.start();
	}

}
