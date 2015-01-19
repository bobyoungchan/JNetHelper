package com.lhy.datapipe;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class PipeCTHInGFW implements Runnable {

	InputStream ori_is;
	OutputStream tar_os;

	public PipeCTHInGFW(InputStream ori_is, OutputStream tar_os) {
		this.ori_is = ori_is;
		this.tar_os = tar_os;
	}

	@Override
	public void run() {

		int index = 0;
		byte[] bt = new byte[1024];
		while (true) {
			try {
				if ((index = ori_is.read(bt, 0, bt.length)) > 0) {
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