package com.lhy.tools;

import java.sql.Statement;
import java.util.Properties;

public class Tools {

	private static Prop prop = new Prop();
	private static Sleep sleep = new Sleep();
	private static Flags flags = new Flags();
	private static DBquery query = new DBquery();
	private static final int RETRY = 5;
	private static final int LOCALPORT = Integer.parseInt(getProp()
			.getProperty("LOCALPORT"));
	private static final int TIMEOUTINGFW = Integer.parseInt(getProp()
			.getProperty("TIMEOUTINGFW"));
	private static final int TIMEOUTOUTGFW = Integer.parseInt(getProp()
			.getProperty("TIMEOUTOUTGFW"));
	private static final String SERVERIP = getProp().getProperty("SERVERIP");
	private static final int SERVERPORTSTART = Integer.parseInt(getProp()
			.getProperty("SERVERPORTSTART"));
	private static final int SERVERPORTCOUNT = Integer.parseInt(getProp()
			.getProperty("SERVERPORTCOUNT"));
	private static final String CERPATH = getProp().getProperty("CERPATH");
	private static final int TESTINGFW = Integer.parseInt(getProp()
			.getProperty("TESTINGFW"));
	private static final String DRIVER = getProp()
			.getProperty("DATABASEDRIVER");
	private static final String URL = getProp().getProperty("DATABASEPATH");
	private static final int VISITINGFW = Integer.parseInt(getProp()
			.getProperty("VISITINGFW"));
	private static final int ISINGFW = Integer.parseInt(getProp().getProperty(
			"ISINGFW"));
	private static final byte[] ACC = { 05, 00 };
	private static final byte[] CONNECT_OK = { 0x5, 0x0, 0x0, 0x1, 0, 0, 0, 0,
			0, 0 };
	private static Sqlite sql = new Sqlite();

	public static byte[] getConnectOK() {
		return CONNECT_OK;
	}

	public static byte[] getAccept() {
		return ACC;
	}

	public static int getIsInGFW() {
		return ISINGFW;
	}

	public static String getSqlURL() {
		return URL;
	}

	public static int getVisitInGFW() {
		return VISITINGFW;
	}

	public static String getSqlDriver() {
		return DRIVER;
	}

	public static int getTestInGFW() {
		return TESTINGFW;
	}

	public static String getCerPath() {
		return CERPATH;
	}

	public static int getServerPortCount() {
		return SERVERPORTCOUNT;
	}

	public static int getServerPortStart() {
		return SERVERPORTSTART;
	}

	public static String getServerIP() {
		return SERVERIP;
	}

	public static int getTimeOutInGFW() {
		return TIMEOUTINGFW;
	}

	public static int getTimeOutOutGFW() {
		return TIMEOUTOUTGFW;
	}

	public static int getLocalPort() {
		return LOCALPORT;
	}

	public static int getRetry() {
		return RETRY;
	}

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
