package com.lhy.tools;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Time {
	public String getTime() {
		return new SimpleDateFormat(" YYYY-MM-dd HH:mm:ss").format(new Date());
	}
}
