package com.lhy.gui;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.lhy.tools.Tools;

public class DefaultJFrame {

	private Thread t1;
	private JFrame f1;
	private JPanel p, p1, p2, p3;
	private JTextArea ta;
	private JTextField upload, download, delayed, count;
	private JScrollPane js;
	private JButton allOut, autoSwitch;
	long start_time = System.currentTimeMillis();

	public DefaultJFrame() {

		f1 = new JFrame("JNetHelper");
		f1.setSize(535, 215);
		f1.setLocationRelativeTo(null);
		f1.setResizable(false);
		f1.setLayout(new BorderLayout());
		f1.setAlwaysOnTop(true);
		f1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		p = new JPanel();
		p1 = new JPanel();
		p2 = new JPanel();
		p3 = new JPanel();

		allOut = new JButton();
		autoSwitch = new JButton();
		if (Tools.getAutoSwitch()) {
			autoSwitch.setEnabled(false);
			allOut.setEnabled(true);
			autoSwitch.setText("当前：智能选择线路");
			allOut.setText("全部流量走国外线路");
		} else {
			autoSwitch.setEnabled(true);
			allOut.setEnabled(false);
			autoSwitch.setText("智能选择线路");
			allOut.setText("当前：全部流量走国外线路");
		}

		upload = Tools.getUpload();
		upload.setText("上行速度:0 Kb/s");
		upload.setFont(new java.awt.Font("Dialog", 1, 12));
		upload.setForeground(Color.BLUE);
		upload.setEditable(false);

		download = Tools.getDownload();
		download.setText("下行速度:0 Kb/s");
		download.setFont(new java.awt.Font("Dialog", 1, 12));
		download.setForeground(Color.BLUE);
		download.setEditable(false);

		count = Tools.getCount();
		count.setText("  当前工作线程数:0 ");
		count.setFont(new java.awt.Font("Dialog", 1, 12));
		count.setForeground(Color.BLUE);
		count.setEditable(false);

		delayed = Tools.getDelayed();
		delayed.setText("  等待连接服务器 ");
		delayed.setFont(new java.awt.Font("Dialog", 1, 12));
		delayed.setForeground(Color.BLUE);
		delayed.setEditable(false);

		ta = Tools.getTA();
		ta.setEditable(false);

		js = new JScrollPane(ta);
		js.setAutoscrolls(true);

		f1.add(p, BorderLayout.NORTH);
		f1.add(js, BorderLayout.CENTER);
		f1.add(p1, BorderLayout.SOUTH);

		p.setLayout(new GridLayout(1, 1));
		p1.setLayout(new GridLayout(1, 1));
		p2.setLayout(new GridLayout(1, 1));
		p3.setLayout(new GridLayout(1, 1));
		p.add(autoSwitch);
		p.add(allOut);
		p1.add(p2);
		p1.add(p3);
		p2.add(upload);
		p2.add(download);
		p3.add(count);
		p3.add(delayed);

		autoSwitch.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Tools.setAutoSwitch(true);
				autoSwitch.setText("当前：智能选择线路");
				allOut.setText("全部流量走国外线路");
				autoSwitch.setEnabled(false);
				allOut.setEnabled(true);
			}

		});
		allOut.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Tools.setAutoSwitch(false);
				autoSwitch.setText("智能选择线路");
				allOut.setText("当前：全部流量走国外线路");
				autoSwitch.setEnabled(true);
				allOut.setEnabled(false);
			}

		});

		Toolkit tk = Toolkit.getDefaultToolkit();
		Image img = tk.getImage("ico/small.png");
		SystemTray systemTray = SystemTray.getSystemTray();
		TrayIcon trayIcon = null;
		try {
			trayIcon = new TrayIcon(img, "JNetHelper is Running\r\n");
			systemTray.add(trayIcon);
			f1.setIconImage(img);
			trayIcon.setImageAutoSize(true);
		} catch (AWTException e2) {
		}
		add_monitor(trayIcon);
		t1 = new Thread(new display_data(trayIcon));
		t1.setPriority(Thread.MIN_PRIORITY);
		t1.start();
	}

	/**
	 * 设置监听
	 */
	private void add_monitor(TrayIcon trayIcon) {
		f1.addWindowListener(new WindowAdapter() {
			public void windowIconified(WindowEvent e) {
				f1.dispose();
			}
		});
		trayIcon.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 1)
					f1.setExtendedState(JFrame.NORMAL);
				f1.setVisible(true);
			}
		});
	}

	/**
	 * 系统托盘显示控制线程
	 *
	 */
	class display_data implements Runnable {
		TrayIcon trayIcon;

		public display_data(TrayIcon trayIcon) {
			this.trayIcon = trayIcon;
		}

		@Override
		public void run() {
			while (true) {
				long now_time = System.currentTimeMillis();
				trayIcon.setToolTip("JNetHelper Is Running Now\r\n"
						+ "Running Times: " + run_time(start_time, now_time));
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
				}
			}
		}
	}

	/**
	 * 将运行时间进行格式化
	 */
	private String run_time(long a, long b) {
		String time_ss, time_sm, time_sh;
		time_ss = time_sm = time_sh = "";
		int time_s, time_m, time_h;
		time_s = (int) (b - a) / 1000;
		time_ss = time_s + "s";
		if (time_s >= 60) {
			time_m = time_s / 60;
			time_s = time_s % 60;
			time_sm = time_m + "m ";
			time_ss = time_s + "s";
			if (time_m >= 60) {
				time_h = time_m / 60;
				time_m = time_m % 60;
				time_sm = time_m + "m ";
				time_sh = time_h + "h ";
			}
		}
		return time_sh + time_sm + time_ss;
	}

	public JFrame getInstance() {
		return f1;
	}
}
