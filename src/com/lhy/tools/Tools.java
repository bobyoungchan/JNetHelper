package com.lhy.tools;

import java.sql.Statement;
import java.util.ArrayList;
import java.util.Properties;

import javax.swing.JTextArea;
import javax.swing.JTextField;

public class Tools {

	private static int ThreadCount = 0;

	private static Prop prop = new Prop();

	private static Sleep sleep = new Sleep();

	private static Flags flags = new Flags();

	private static boolean autoSwitch = true;

	private static DBquery query = new DBquery();

	private static final int RETRY = 5;

	private static JTextArea ta = new JTextArea();

	private static JTextField upload = new JTextField();

	private static JTextField download = new JTextField();

	private static JTextField delayed = new JTextField();

	private static JTextField count = new JTextField();

	private static ArrayList<String> array = new ArrayList<String>();

	private static final int LOCALPORT = Integer.parseInt(getProp()
			.getProperty("LOCALPORT"));

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

	private static final byte[] ACC5 = { 05, 00 };

	private static final byte[] CONNECT_OK = { 0x5, 0x0, 0x0, 0x1, 0, 0, 0, 0,
			0, 0 };

	private static Sqlite sql = new Sqlite();

	public synchronized static ArrayList<String> getArray() {
		return array;
	}

	public static byte[] getConnectOK() {
		return CONNECT_OK;
	}

	public static byte[] getAccepted5() {
		return ACC5;
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

	public synchronized static void countPlus() {
		ThreadCount++;
	}

	public synchronized static void countCut() {
		ThreadCount--;
	}

	public static int getThreadCount() {
		return ThreadCount;
	}

	public static JTextArea getTA() {
		return ta;
	}

	public static JTextField getUpload() {
		return upload;
	}

	public static JTextField getDownload() {
		return download;
	}

	public static JTextField getDelayed() {
		return delayed;
	}

	public static JTextField getCount() {
		return count;
	}

	public static void setAutoSwitch(boolean auto) {
		autoSwitch = auto;
	}

	public static boolean getAutoSwitch() {
		return autoSwitch;
	}

}
