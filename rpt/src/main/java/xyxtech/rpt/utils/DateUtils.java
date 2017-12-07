package xyxtech.rpt.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {

	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
	
	private static final SimpleDateFormat hourSdf = new SimpleDateFormat("HH");
	
	public static final int expireTime = 25*60*60;
	
	public static String getDayStr(long time){
		return sdf.format(new java.util.Date(time));
	}
	
	public static int getHour(long time){
		return Integer.parseInt(hourSdf.format(new java.util.Date(time)));
	}
	public static int getExpiretime() {
		return expireTime;
	}
	
	public static Date afterDate(Date current,int afterNum){
		Calendar calendar = Calendar.getInstance(); 
		calendar.setTime(current);
		calendar.add(Calendar.DAY_OF_MONTH, afterNum); 
		return calendar.getTime();
	}
	
}
