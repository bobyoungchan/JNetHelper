package com.lhy.tools;

public class DBquery {

	public String isOutGFW(String host) {
		return "select * from OutGFW where host = '" + host + "'";
	}

	public String putOutGFW(String host) {
		return "insert into OutGFW values('" + host + "')";
	}

	public String delOutGFW(String host) {
		return "delete from OutGFW where host ='" + host + "'";
	}

	public String isInGFW(String host) {
		return "select * from InGFW where host = '" + host + "'";
	}

	public String putInGFW(String host) {
		return "insert into InGFW values('" + host + "')";
	}

	public String delInGFW(String host) {
		return "delete from InGFW where host ='" + host + "'";
	}
}
