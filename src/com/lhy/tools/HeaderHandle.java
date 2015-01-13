package com.lhy.tools;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HeaderHandle {

	private int port, num, dou_num;
	private boolean add;
	private String all, head, str, host, kind, host_last, _index;

	HeaderHandle() {
		port = 80;
		add = false;
		all = str = head = host = kind = _index = host_last = "";
	}

	public String[] getChangedHeader(String header) {
		all = header;
		get_host_port();
		change_http_head();
		String[] info = new String[4];
		info[0] = host;
		info[1] = port + "";
		info[2] = head;
		info[3] = all;
		return info;
	}

	/**
	 * 修改HTTP请求的头部
	 */
	private void change_http_head() {
		if (!add)
			all = all.replace(_index, host_last);
		else
			all = all.replace(_index, "/");
	}

	/**
	 * 获取请求所需要使用的端口
	 */
	private int get_port(String string) {
		String _str = string;
		String fin = "";
		char[] c = _str.toCharArray();
		Pattern p = Pattern.compile("[\\d]{0,4}");
		for (int i = 0; i < c.length; i++) {
			Matcher m = p.matcher((c[i] + ""));
			if (m.matches()) {
				fin += c[i];
				if (fin.length() == 5)
					break;
			}
		}
		return Integer.parseInt(fin);
	}

	/**
	 * 判断，并获取目标主机地址
	 */
	private void get_host_port() {
		int n_1 = all.indexOf("\r\n");
		if (n_1 != -1) {
			int line_num = 0;
			while (line_num < all.split("\r\n").length) {
				str = (all.split("\r\n")[0]);
				if (str.indexOf("HTTP/1.") != -1)
					break;
			}
		}
		if ((str != null) && (str.indexOf(" ") != -1)) {
			head = str.split(" ")[0];
			host = str.split(" ")[1];
			_index = host;
			dou_num = host.indexOf("//");
			if (dou_num != -1) {
				kind = host.split("//")[0] + "//";
				host = host.replace(kind, "");
			}
			int n = host.indexOf("/");
			if (n != -1) {
				host_last = host.substring(n);
				host = host.substring(0, n);
			} else {
				add = true;
			}
			num = host.indexOf(":");
			if (num != -1) {
				port = get_port((host.split(":")[1]));
				host = host.split(":")[0];
			}
		}
	}

}
