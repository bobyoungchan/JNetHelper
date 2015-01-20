package com.lhy.gui;

import com.lhy.tools.Tools;

public class DataCount implements Runnable {

	@Override
	public void run() {
		while (true) {
			while (Tools.getConnected()) {
				long upfirst = Tools.getUpspeed();
				long downfirst = Tools.getDownspeed();
				Tools.sleep(1000);
				long upsecond = Tools.getUpspeed();
				long downsecond = Tools.getDownspeed();
				Tools.resetSpeed();
				Tools.getUpload().setText(
						"上行速度:" + (upsecond - upfirst) / 1024 + " Kb/s");
				Tools.getDownload().setText(
						"下行速度:" + (downsecond - downfirst) / 1024 + " Kb/s");
				Tools.getCount().setText("  当前工作线程数:" + Tools.getThreadCount());
			}
			Tools.sleep(2000);
		}
	}

}
