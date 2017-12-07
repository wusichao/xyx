package com.xyx.ls.util;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.*;
import java.util.*;
import com.mysql.jdbc.Connection;

public class JDBCConnect {
	private static Connection getConn() {
	    String driver = "com.mysql.jdbc.Driver";
	    String url = "jdbc:mysql://127.0.0.1:3306/tk";
	    String username = "root";
	    String password = "123456";
	    Connection conn = null;
	    try {
	        Class.forName(driver); //classLoader,加载对应驱动
//	        Enumeration enum=DriverManager.getDrivers();
	        System.out.println(DriverManager.getDrivers().nextElement());
	        conn = (Connection) DriverManager.getConnection(url, username, password);
	    } 
	    catch (ClassNotFoundException e) {
	        e.printStackTrace();
	    } 
	    catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return conn;
	}
	public static void main(String[] args) {
		System.out.println(getConn().toString());
	}
}
