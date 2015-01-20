package com.lhy.datapipe;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.lhy.tools.Tools;

public class PipeCTH implements Runnable {

	InputStream ori_is;
	OutputStream tar_os;

	public PipeCTH(InputStream ori_is, OutputStream tar_os) {
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
					Tools.setUpspeed(index);
					tar_os.write(bt, 0, index);
					tar_os.flush();
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
