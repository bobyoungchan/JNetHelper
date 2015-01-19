package com.lhy.tools;

public class DBquery {

	public String isOutGFW(String host) {
		return "select * from OutGFW where host like '%" + getHost(host) + "%'";
	}

	public String putOutGFW(String host) {
		return "insert into OutGFW values('" + getHost(host) + "')";
	}

	public String delOutGFW(String host) {
		return "delete from OutGFW where host like '%" + getHost(host) + "%'";
	}

	public String isInGFW(String host) {
		return "select * from InGFW where host like '%" + getHost(host) + "%'";
	}

	public String putInGFW(String host) {
		return "insert into InGFW values('" + getHost(host) + "')";
	}

	public String delInGFW(String host) {
		return "delete from InGFW where host like '%" + getHost(host) + "%'";
	}

	private String getHost(String host) {
		if (!host
				.matches("([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}"))
			host = host.split("\\.")[host.split("\\.").length - 2];
		return host;
	}
}
