package com.lhy.gui;

import javax.swing.JFrame;

import com.lhy.delayed.Delayed;

public class Start {

	JFrame f;
	private Thread t;

	Start() {
		UIinit();
	}

	private void UIinit() {
		f = new DefaultJFrame().getInstance();
		f.setVisible(true);
		t = new Thread(new Delayed());
		t.setPriority(Thread.MIN_PRIORITY);
		t.start();
	}

	public static void main(String[] args) {
		new Start();
	}
}
