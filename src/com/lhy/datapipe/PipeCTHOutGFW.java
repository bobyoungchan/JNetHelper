package com.lhy.datapipe;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class PipeCTHOutGFW implements Runnable {
	OutputStream tar_os;
	InputStream ori_is;
	String all;

	/**
	 * 该线程用于将收到的请求转发到国外服务器上再有服务器上的程序处理并转发
	 * 
	 */
	public PipeCTHOutGFW(String all, InputStream ori_is, OutputStream tar_os) {
		this.ori_is = ori_is;
		this.tar_os = tar_os;
		this.all = all;
	}

	@Override
	public void run() {
		int index = 0;
		byte[] bt = new byte[1024];
		try {
			tar_os.write(all.getBytes());
		} catch (IOException e) {
		}
		while (true) {
			try {
				if ((index = ori_is.read(bt, 0, bt.length)) > 0) {
					tar_os.write(bt, 0, index);
				} else if (index <= 0) {
					break;
				}
			} catch (IOException e) {
				break;
			}
		}
		try {
			tar_os.close();
			ori_is.close();
		} catch (IOException e) {
		}
	}
}
