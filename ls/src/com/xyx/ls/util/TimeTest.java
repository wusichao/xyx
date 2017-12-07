package com.xyx.ls.util;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.ResultSetMetaData;
import com.mysql.jdbc.Statement;

@Controller
public class TimeTest {
	private static Connection getConn() {
//	    String driver = "com.mysql.jdbc.Driver";
//	    String url = "jdbc:mysql://101.201.49.35:9600/wsctk";
//	    String username = "ls";
//	    String password = "Ls1107!$";
	    Connection conn = null;
//	    try {
//	        Class.forName(driver); //classLoader,加载对应驱动
//	        conn = (Connection) DriverManager.getConnection(url, username, password);
//	    } catch (ClassNotFoundException e) {
//	        e.printStackTrace();
//	    } catch (SQLException e) {
//	        e.printStackTrace();
//	    }
	    return conn;
	}
	private static List select(String sql) {
		 List list = new ArrayList(); 
	    Connection conn = getConn();
	    try {
//	        pstmt = (PreparedStatement) conn.prepareStatement(sql);
//	        i = pstmt.executeUpdate();
	        
	        Statement stmt = (Statement) conn.createStatement();
	        ResultSet rs = stmt.executeQuery(sql);
	     ResultSetMetaData md = (ResultSetMetaData) rs.getMetaData(); 
	        int columnCount = md.getColumnCount(); 
	        while (rs.next()) {
	         for (int i = 1; i <= columnCount; i++) { 
	          list.add(rs.getObject(i)); 
	         }
	        } 
	        stmt.close();
	        conn.close();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return list;
	}
	public static void main(String[] args) {
//		 timer1();
//		 timer2();
//		 timer3();
		 timer4();
	}

	// 第一种方法：设定指定任务task在指定时间time执行 schedule(TimerTask task, Date time)
	public static void timer1() {
		System.out.println("wsc");
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			public void run() {
				System.out.println("15小时到了，我要发邮件了");
				String sql="SELECT email, end_date FROM account INNER JOIN orders ON account.id=orders.account_id WHERE  DATEDIFF(end_date,NOW())<=7";
				List wsclist=select(sql);
				for(Object str:wsclist){
					System.out.println(str);
				}
			}
		}, 1000*5);// 设定指定的时间time,此处为15小时
	}
	
	// 第四种方法：安排指定的任务task在指定的时间firstTime开始进行重复的固定速率period执行．
	// Timer.scheduleAtFixedRate(TimerTask task,Date firstTime,long period)
	public static void timer4() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 24); // 控制时
		calendar.set(Calendar.MINUTE, 0); // 控制分
		calendar.set(Calendar.SECOND, 0); // 控制秒
		Date time = calendar.getTime(); // 得出执行任务的时间,此处为今天的12：00：00
		Timer timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {
			public void run() {
				System.out.println("新的一天开始了，我查询下");
				System.out.println("15小时后我将发邮件");
				timer1();
			}
		}, time, 1000 * 60);// 这里设定将延时每天固定执行
	}
}
