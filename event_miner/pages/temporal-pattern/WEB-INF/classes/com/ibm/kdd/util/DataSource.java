package com.ibm.kdd.util;

import java.sql.*;

import javax.sql.*;

public class DataSource {

	public static Connection getConnection(String db)
			throws ClassNotFoundException, SQLException {
		Connection conn = null;
		String driver = "COM.ibm.db2os390.sqlj.jdbc.DB2SQLJDriver";
		String url = "jdbc:db2://combpm2.watson.ibm.com:50000/" + db;
		String user = "db2admin";
		String passwd = "db2admin";
		Class.forName(driver);

		conn = DriverManager.getConnection(url, user, passwd);

		return conn;
	}

	public static Connection getDWConnection()
			throws ClassNotFoundException, SQLException {
		Connection conn = null;
		String driver = "COM.ibm.db2os390.sqlj.jdbc.DB2SQLJDriver";
//		String url = "jdbc:db2://combpm2.watson.ibm.com:50000/" + db;
		String url = "jdbc:db2://b03cxnp01028.gho.boulder.ibm.com:50004/SMIWSLA";
		String user = "ldwuser";
		String passwd = "ldwag1234";
		Class.forName(driver);

		conn = DriverManager.getConnection(url, user, passwd);

		return conn;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
