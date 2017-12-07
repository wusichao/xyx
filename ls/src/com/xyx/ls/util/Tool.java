package com.xyx.ls.util;

import java.awt.Color;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.net.URLDecoder;
import java.util.UUID;


public class Tool {

	/**
	 * 
	 * 获取UUID
	 * 
	 * @return String UUID
	 */
	public static String getUUID() {
		return UUID.randomUUID().toString();
	}

	/**
	 * 
	 * 判断字符串是否相等
	 * 
	 * @param str1
	 *            String 字符串1
	 * @param str2
	 *            String 字符串2
	 * @return boolean true：相等 false：不相等
	 */
	public static boolean checkStringSame(String str1, String str2) {

		if (Tool.isNullOrEmtryTrim(str1) && Tool.isNullOrEmtryTrim(str2)) {// 都为空
			return true;
		} else if (!Tool.isNullOrEmtryTrim(str1) && str1.equals(str2)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 
	 * 校验给定的颜色是否相等
	 * 
	 * @param oldColor
	 *            Color 颜色1
	 * @param newColor
	 *            Color 颜色2
	 * @return boolean true:颜色1和颜色2相等；false:颜色1和颜色2不一样
	 */
	public static boolean checkColorSame(Color oldColor, Color newColor) {
		return ((oldColor == null && newColor == null) || (oldColor != null && oldColor
				.equals(newColor))) ? true : false;
	}

	/**
	 * 
	 * 给定颜色RGB字符串返回Color对象
	 * 
	 * @param colorStr
	 *            String [A,B,C]格式
	 * @return Color 参数为空返回画图面板面板颜色，否则返回和参数对应的颜色
	 */
	public static Color covertStringToColor(String colorStr) {
		if (Tool.isNullOrEmtryTrim(colorStr)) {
			return null;
		}
		// RBC
		String[] eleArrs = colorStr.split(",");
		if (eleArrs == null || eleArrs.length != 3) {
			return null;
		}
		return new Color(coverStringToInt(eleArrs[0]),
				coverStringToInt(eleArrs[1]), coverStringToInt(eleArrs[2]));
	}

	/**
	 * 
	 * 
	 * 字符串数字转换成整形
	 * 
	 * @param eleStr
	 *            String 字符串数字
	 * @return int
	 */
	public static int coverStringToInt(String eleStr) {
		if (Tool.isNullOrEmtryTrim(eleStr)) {
			return 0;
		}
		return Integer.valueOf(eleStr);
	}

	/**
	 * 
	 * 颜色对象转换成RGB字符串
	 * 
	 * @param color
	 *            Color 颜色对象
	 * @return String [A,B,C]格式 或“”字符串
	 */
	public static String coverColorToString(Color color) {
		if (color == null) {
			return "";
		}
		return String.valueOf(color.getRed() + "," + color.getGreen() + ","
				+ color.getBlue());
	}

	/**
	 * 
	 * 整形0、1转换成boolean型true、false
	 * 
	 * 0:false 1:true
	 * 
	 * @param param
	 *            int (0,1)
	 * @return boolean 0:false 1:true
	 */
	public static boolean converIntToBoolean(int param) {
		return (param == 0) ? false : true;
	}

	/**
	 * 
	 * boolean型true、false 转换成整形0、1
	 * 
	 * 0:false 1:true
	 * 
	 * @param param
	 *            boolean
	 * @return int 0:false 1:true
	 */
	public static int converBooleanToInt(boolean param) {
		return param ? 1 : 0;
	}

	/**
	 * 
	 * 给定参数先去空再判断是否为null或空
	 * 
	 * @param str
	 *            String
	 * @return boolean str为空返回ture，不为空返回false
	 */
	public static boolean isNullOrEmtryTrim(String str) {
		return (str == null || str.trim().isEmpty()) ? true : false;
	}

	/**
	 * 
	 * 给定参数判断是否为null或空,如果需要去空再判断使用方法isNullOrEmtryTrim
	 * 
	 * @param str
	 *            String
	 * @return boolean str为空返回ture，不为空返回false
	 */
	public static boolean isNullOrEmtry(String str) {
		return (str == null || str.isEmpty()) ? true : false;
	}

	

	/**
	 * 
	 * 名称不能大于61个汉字或122字母
	 * 
	 * @param name
	 * @return 超过返回true，没有超过返回false
	 */
	public static boolean checkNameMaxLength(String name) {
		return (ParameterCheckUtil.getTextLength(name) > 122) ? true : false;
	}

	/**
	 * 
	 * 密码不能大于10个汉字或19字母
	 * 
	 * @param name
	 * @return 超过返回true，没有超过返回false
	 */
	public static boolean checkPasswordMaxLength(String name) {
		return (ParameterCheckUtil.getTextLength(name) > 19) ? true : false;
	}

	/***************************************************************************
	 * 验证输入内容不能大于600个汉字或1200字符
	 * 
	 * @param name
	 * @return 超过返回true，没有超过返回false
	 */
	public static boolean checkNoteLength(String name) {
		return (ParameterCheckUtil.getTextLength(name) > 1200) ? true : false;
	}

	/**
	 * 
	 * 获取给定值的四舍五入（精确到小数点后3位）的值
	 * 
	 * @param value
	 *            double
	 * @return double
	 */
	public static double convertDouble(double value) {
		long valLong = Math.round(value * 1000); // 四舍五入
		return valLong / 1000.0;
	}

	/**
	 * 
	 * 四舍五入获取double值的int值
	 * 
	 * @param doubleObj
	 * @return int
	 */
	public static int convertDoubleToInt(double doubleObj) {
		return (int) Math.floor(doubleObj + 0.5);
	}

	/**
	 * 传入String型的Color 返回Color
	 * 
	 * @param tone
	 *            String的 color
	 * @return
	 */
	public static Color getColorByString(String tone) {
		Color color = null;
		String colorStr[] = tone.split(",");
		if (colorStr.length > 0) {
			color = new Color(Integer.valueOf(colorStr[0]), Integer
					.valueOf(colorStr[1]), Integer.valueOf(colorStr[2]));
		}
		return color;
	}

	
	

	/**
	 * 反射获取类中指定方法
	 * 
	 * @param name
	 * @return
	 * @throws Exception
	 */
	public static Object invokeMethod(String name, String strMethod)
			throws Exception {
		// 获取class
		Class<?> cl = Class.forName(name);
		// 创建对象
		Object obj = cl.newInstance();
		// 获取方法
		Method method = cl.getMethod(strMethod);
		return method.invoke(obj);//
	}

	
	/**
	 * 字符串的回车符转化成HTML中的换行符<BR>
	 * 
	 * @param string
	 *            活动说明或关键活动说明
	 * @return 替换后的字符串
	 */
	public static StringBuffer getsubstring(String string) {
		StringBuffer stringBuffer = new StringBuffer();
		if (Tool.isNullOrEmtryTrim(string)) {
			return stringBuffer;
		}
		String[] str = string.split("\n");
		for (int i = 0; i < str.length; i++) {
			int s = 0;
			int stringLength = 50;
			if (str[i].length() == 0) {
				s = str[i].length() / stringLength;
			} else {
				s = str[i].length() / stringLength + 1;
			}
			for (int j = 0; j < s; j++) {
				if (str[i].length() < stringLength) {
					string = str[i] + "\n";
				} else {
					string = str[i].substring(0, stringLength) + "\n";
					str[i] = str[i].substring(stringLength);
				}
				string = string.replaceAll("\n", "<br>");
				stringBuffer.append(string);
			}
		}
		return stringBuffer;
	}
	public static String decode2(String s){
		if(s==null)
			return "";
		try{
			return URLDecoder.decode(s,"gbk");
		}catch(UnsupportedEncodingException e){
			return "";
		}
	}
	/** 
	 * 获得字符集编码类型 
	 * @param str  
	 * @return 返回字符编码类型 
	 */  
	public static String getCharEncode(String str){  
	      
	    String charEncode = "ISO-8859-1";  
	    try {  
	        if(str.equals(new String(str.getBytes(charEncode),charEncode))){  
	              
	            return charEncode;  
	        }  
	    } catch (UnsupportedEncodingException e) {  
	          
	    }  
	      
	    charEncode = "GB2312";  
	    try {  
	        if(str.equals(new String(str.getBytes(charEncode),charEncode))){  
	              
	            return charEncode;  
	        }  
	    } catch (UnsupportedEncodingException e) {  
	          
	    }  
	      
	    charEncode = "GBK";  
	    try {  
	        if(str.equals(new String(str.getBytes(charEncode),charEncode))){  
	              
	            return charEncode;  
	        }  
	    } catch (UnsupportedEncodingException e) {  
	          
	    }  
	      
	    charEncode = "UTF-8";  
	    try {  
	        if(str.equals(new String(str.getBytes(charEncode),charEncode))){  
	          
	            return charEncode;  
	        }  
	    } catch (UnsupportedEncodingException e) {  
	      
	    }  
	      
	    return "";  
	      
	}  
	  
	/** 
	 * 转成GBK编码 
	 * @param str 
	 * @return 
	 */  
	public String transcodeToGBK(String str){  
	    return transcode(str,"GBK");  
	}  
	  
	/** 
	 * 转成UTF-8编码 
	 * @param str 
	 * @return 
	 */  
	public String transcodeToUTF_8(String str){  
	    return transcode(str,"UTF-8");  
	}  
	  
	/** 
	 * 自动转码 
	 * @param str 
	 * @param charEncode 默认UTF-8 
	 * @return 
	 */  
	public static String transcode(String str,String charEncode){  
	      
	    if(null==charEncode||"".equals(charEncode)){  
	        charEncode = "UTF-8";  
	    }  
	      
	    String temp = "";  
	    try {  
//	        String code = getCharEncode(str);  
//	        LogUtils.logI("编码："+code);  
	        temp = new String(str.getBytes("UTF-8"),charEncode);  
	    } catch (UnsupportedEncodingException e) {  
//	        LogUtils.logE("转码异常："+e.getMessage());  
	    }  
	    return temp;  
	} 
}
