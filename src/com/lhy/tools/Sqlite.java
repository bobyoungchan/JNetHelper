package com.lhy.tools;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Sqlite {

	Connection conn = null;

	public Sqlite() {
		try {
			Class.forName(Tools.getSqlDriver());
			conn = DriverManager.getConnection(Tools.getSqlURL());
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public Statement getConn() {
		try {
			return conn.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
}
