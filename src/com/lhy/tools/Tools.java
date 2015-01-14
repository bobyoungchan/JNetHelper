package com.lhy.tools;

import java.util.Properties;

public class Tools {

	private static Prop prop = new Prop();
	private static Sleep sleep = new Sleep();
	private static Flags flags = new Flags();
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

}
