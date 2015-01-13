package com.lhy.tools;

import java.util.Properties;

public class Tools {

	private static Prop prop = new Prop();
	private static Sleep sleep = new Sleep();
	private static Flags flags = new Flags();
	private static HeaderHandle handle = new HeaderHandle();
	public static final int LOCALPORT = Integer.parseInt(getProp().getProperty(
			"LOCALPORT"));
	public static final int TIMEOUTINGFW = Integer.parseInt(getProp()
			.getProperty("TIMEOUTINGFW"));

	public static String[] getChangedHeader(String header) {
		return handle.getChangedHeader(header);
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

}
