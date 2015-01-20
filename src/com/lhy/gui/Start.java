package com.lhy.gui;

import java.io.IOException;
import java.net.ServerSocket;

import javax.swing.JFrame;

import com.lhy.delayed.Delayed;
import com.lhy.localmonitor.MonitorWorker;
import com.lhy.tools.Tools;

public class Start {

	JFrame f;
	private Thread t, t1, t2;
	private ServerSocket server;

	Start() {
		UIinit();
		Netinit();
	}

	private void Netinit() {
		Tools.setTa("程序初始化中...");
		t = new Thread(new Delayed());
		t.setPriority(Thread.MIN_PRIORITY);
		t.start();
		t1 = new Thread(new DataCount());
		t1.setPriority(Thread.MIN_PRIORITY);
		t1.start();
		try {
			server = new ServerSocket(Tools.getLocalPort());
			t2 = new Thread(new MonitorWorker(server));
			t2.setPriority(Thread.MAX_PRIORITY);
			t2.start();
		} catch (IOException e) {
		}
	}

	private void UIinit() {
		f = new DefaultJFrame().getInstance();
		f.setVisible(true);
	}

	public static void main(String[] args) {
		new Start();
	}
}
