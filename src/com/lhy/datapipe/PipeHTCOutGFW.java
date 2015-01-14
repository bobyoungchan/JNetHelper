package com.lhy.datapipe;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * This is the channel between the host to the client in GFW.
 * 
 * @author lhy
 *
 */
public class PipeHTCOutGFW implements Runnable {
	InputStream tar_is;
	OutputStream ori_os;

	/**
	 * 该线程用于接收国外服务器传送回来的数据，并返还给浏览器
	 * 
	 * @param outs
	 * @param ssl
	 */
	public PipeHTCOutGFW(InputStream tar_is, OutputStream ori_os) {
		this.tar_is = tar_is;
		this.ori_os = ori_os;
	}

	@Override
	public void run() {
		int index = 0;
		byte[] bt = new byte[1024];
		while (true) {
			try {
				if ((index = tar_is.read(bt, 0, bt.length)) > 0) {
					ori_os.write(bt, 0, index);
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
}
