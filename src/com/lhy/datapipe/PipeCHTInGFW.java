package com.lhy.datapipe;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class PipeCHTInGFW implements Runnable {

	InputStream ori_is;
	OutputStream tar_os;
	boolean isHTTPS;

	public PipeCHTInGFW(InputStream ori_is, OutputStream tar_os, boolean isHTTPS) {
		this.ori_is = ori_is;
		this.tar_os = tar_os;
		this.isHTTPS = isHTTPS;
	}

	@Override
	public void run() {

		int index = 0;
		byte[] bt = new byte[1024];
		while (true) {
			try {
				if ((index = ori_is.read(bt, 0, bt.length)) > 0) {
					if (!isHTTPS)
						get_real_head(bt, index);
					else
						tar_os.write(bt, 0, index);
					if (ori_is.available() < 0)
						break;
				} else if (index <= 0) {
					break;
				}
			} catch (IOException e) {
				break;
			}
		}
		try {
			ori_is.close();
			tar_os.close();
		} catch (IOException e) {
		}
	}

	// 对http请求进行判断和进行修改
	private void get_real_head(byte[] _bt, int _index) {
		String head = new String(_bt, 0, _index);
		String line1, totle_head, new_last, ori_host, change_last, change_line = "";
		ori_host = head;
		try {
			if ((head.indexOf("\r\n") != -1) && (!head.equals("\r\n"))
					&& (!head.startsWith("\r\n"))) {
				line1 = head.split("\r\n")[0];
				if (line1.indexOf(" ") != -1) {
					totle_head = line1.split(" ")[1];
					if (totle_head.startsWith("http")) {
						new_last = totle_head.replace(
								totle_head.split("//")[0], "").substring(2);
						if (new_last.indexOf("/") != -1) {
							change_last = new_last.substring(new_last
									.indexOf("/"));
							change_line = line1
									.replace(totle_head, change_last);
							ori_host = ori_host.replace(line1, change_line);
							tar_os.write(ori_host.getBytes());
						} else
							tar_os.write(ori_host.replace(line1, "/")
									.getBytes());
					} else
						tar_os.write(_bt, 0, _index);
				} else
					tar_os.write(_bt, 0, _index);
			} else
				tar_os.write(_bt, 0, _index);
		} catch (IOException e) {
		}
	}
}
