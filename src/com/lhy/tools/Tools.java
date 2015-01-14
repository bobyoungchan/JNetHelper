package com.lhy.tools;

import java.sql.Statement;
import java.util.Properties;

public class Tools {

	private static Prop prop = new Prop();
	private static Sleep sleep = new Sleep();
	private static Flags flags = new Flags();
	private static DBquery query = new DBquery();
	public static final int RETRY = 5;
	public static final int LOCALPORT = Integer.parseInt(getProp().getProperty(
			"LOCALPORT"));
	public static final int TIMEOUTINGFW = Integer.parseInt(getProp()
			.getProperty("TIMEOUTINGFW"));
	public static final int TIMEOUTOUTGFW = Integer.parseInt(getProp()
			.getProperty("TIMEOUTOUTGFW"));
	public static final String SERVERIP = getProp().getProperty("SERVERIP");
	public static final int SERVERPORTSTART = Integer.parseInt(getProp()
			.getProperty("SERVERPORTSTART"));
	public static final int SERVERPORTCOUNT = Integer.parseInt(getProp()
			.getProperty("SERVERPORTCOUNT"));
	public static final String CERPATH = getProp().getProperty("CERPATH");
	public static final int TESTINGFW = Integer.parseInt(getProp().getProperty(
			"TESTINGFW"));
	public static final String DRIVER = getProp().getProperty("DATABASEDRIVER");
	public static final String URL = getProp().getProperty("DATABASEPATH");
	public static final int VISITINGFW = Integer.parseInt(getProp()
			.getProperty("VISITINGFW"));
	public static final int ISINGFW = Integer.parseInt(getProp().getProperty(
			"ISINGFW"));
	private static Sqlite sql = new Sqlite();

	public static Properties getProp() {
		return prop.getProp();
	}

	public static void sleep(int millis) {
		sleep.sleep(millis);
	}

	public static Boolean getServerMonitorFlag() {
		return flags.getServerMonitorFlag();
	}

	public static void setServerMonitorFlag(boolean flag) {
		flags.setServerMonitorFlag(flag);
	}

	public synchronized static Statement getSql() {
		return sql.getConn();
	}

	public synchronized static String isOutGFW(String host) {
		return query.isOutGFW(host);
	}

	public synchronized static String putOutGFW(String host) {
		return query.putOutGFW(host);
	}

	public synchronized static String delOutGFW(String host) {
		return query.delOutGFW(host);
	}

	public synchronized static String isInGFW(String host) {
		return query.isInGFW(host);
	}

	public synchronized static String putInGFW(String host) {
		return query.putInGFW(host);
	}
	
	public synchronized static String delInGFW(String host) {
		return query.delInGFW(host);
	}

}
