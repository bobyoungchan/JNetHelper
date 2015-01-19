package com.lhy.datahandle;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class NewRequest implements Runnable {

	private byte[] bt = new byte[1024];
	private Socket socket;
	private InputStream ori_is;
	private OutputStream ori_os;

	public NewRequest(Socket socket) {
		this.socket = socket;
	}

	@Override
	public void run() {
		initialization();
	}

	private void initialization() {
		try {
			ori_is = socket.getInputStream();
			ori_os = socket.getOutputStream();
			if (isSocks5()) {
				Thread t = new Thread(new Socks5(ori_is, ori_os));
				t.setPriority(Thread.MAX_PRIORITY);
				t.start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private boolean isSocks5() {
		try {
			ori_is.read(bt, 0, bt.length);
		} catch (IOException e) {
			e.printStackTrace();
		}
		if ((bt[0] + "").equals("5"))
			return true;
		else
			return false;
	}
}
