package com.lhy.delayed;

import java.awt.Color;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

import javax.swing.JTextField;

import com.lhy.tools.Tools;

public class Delayed implements Runnable {

	Socket s;
	InputStream is;
	OutputStream os;
	JTextField tf;

	public Delayed() {
		this.tf = Tools.getDelayed();
	}

	@Override
	public void run() {
		connect();
		while (true) {
			int index = 0;
			String info = "";
			long time_a = System.currentTimeMillis();
			int size = (time_a + "").length();
			try {
				os.write((time_a + "").getBytes());
				while (index < size) {
					int l = is.read();
					info += (char) l;
					index = info.length();
				}
				long ms = (System.currentTimeMillis() - Long.valueOf(info)) / 2;
				if (ms < 300)
					tf.setForeground(Color.green);
				else if (300 <= ms && ms < 500)
					tf.setForeground(Color.orange);
				else
					tf.setForeground(Color.red);
				tf.setText("    延迟: " + ms + " ms");
			} catch (IOException e) {
				tf.setForeground(Color.red);
				tf.setText("    网络异常");
				Tools.setConnected(false);
				Tools.sleep(1000);
				connect();
			}
			Tools.sleep(1000);
		}
	}

	private void connect() {
		try {
			s = new Socket();
			s.connect(new InetSocketAddress("104.236.158.110", 10510), 3000);
			s.setSoTimeout(10000);
			is = s.getInputStream();
			os = s.getOutputStream();
			Tools.setConnected(true);
		} catch (IOException e) {
			try {
				s.close();
				Tools.setConnected(false);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			Tools.sleep(1000);
			connect();
		}
	}
}
