package com.lhy.datapipe;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.lhy.tools.Tools;

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
						tar_os.write(Tools.getChangedHeader(new String(bt, 0,
								index))[3].getBytes());
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

}
