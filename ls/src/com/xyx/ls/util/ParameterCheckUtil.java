package com.xyx.ls.util;

import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 
 * 
 * 用户输入校验工具类
 * 
 * 
 * @author ZHOUXY
 * 
 */
public class ParameterCheckUtil {

	/**
	 * 
	 * 校验名称是否满足要求,验证（名称不为空，字符正确，长度）成功返回“”，否则返回相应提示信息
	 * 
	 * @param name
	 *            String 名称
	 * @return String 验证（名称不为空，字符正确，长度）成功返回""，否则返回相应提示信息
	 */
	public static String checkName(String name) {
		if (Tool.isNullOrEmtryTrim(name)) {
			// 名称不能为空
			return ParameterCheckInfoData.getNameNotNull();
		} else if (!checkNameFileSpecialChar(name)) {
			// 名称不能包括任意字符之一: \ / : * ? < > | # %
			return ParameterCheckInfoData.getNameErrorCharInfo();
		} else if (getTextLength(name) > 122) {
			// 不能超过122个字符或61个汉字
			return ParameterCheckInfoData.getNameLengthInfo();
		}
		return "";
	}

	/**
	 * 
	 * 校验名称是否满足要求,验证（名称不为空，字符正确，长度）成功返回“”，否则返回相应提示信息
	 * 
	 * @param name
	 *            String 名称
	 * @return String 验证（名称不为空，字符正确，长度）成功返回""，否则返回相应提示信息
	 */
	public static String checkNullName(String name) {
		if (Tool.isNullOrEmtryTrim(name)) {
			// 名称不能为空
			return ParameterCheckInfoData.getNameNotNull();
		} else if (getTextLength(name) > 122) {
			// 不能超过122个字符或61个汉字
			return ParameterCheckInfoData.getNameLengthInfo();
		}
		return "";
	}

	/**
	 * 输入内容允许为空 (长度是否在规定范围，是否包含特殊字符)成功返回""，否则返回相应提示信息
	 * 
	 * @param name
	 *            String 名称
	 * @return String 验证（字符正确，长度）成功返回""，否则返回相应提示信息
	 */
	public static String checkNameContainsNull(String name) {
		if (getTextLength(name) > 122) {
			// 不能超过122个字符或61个汉字
			return ParameterCheckInfoData.getNameLengthInfo();
		}
		return "";
	}

	/**
	 * 
	 * 校验名称是否满足要求,验证（名称不为空，字符正确，长度）成功返回“”，否则返回相应提示信息
	 * 
	 * @param name
	 *            String 名称
	 * @return String 验证（名称不为空，字符正确，长度）成功返回""，否则返回相应提示信息
	 */
	public static String checkAreaName(String name) {
		if (Tool.isNullOrEmtryTrim(name)) {
			// 名称不能为空
			return ParameterCheckInfoData.getNameNotNull();
		} else if (getTextLength(name) > 1200) {
			// 不能超过1200个字符或600个汉字
			return ParameterCheckInfoData.getNoteLengthInfo();
		}
		return "";
	}

	/**
	 * 
	 * 判断给定字符串长度是否大于给定长度
	 * 
	 * @param name
	 *            字符串
	 * @param length
	 *            长度
	 * @return boolean true：不大于；false：大于
	 */
	public static boolean checkLength(String name, int length) {
		if (getTextLength(name) > length) {
			return false;
		}
		return true;
	}

	/**
	 * 
	 * 判断给定参数是否超过122个字符或61个汉字
	 * 
	 * @param name
	 *            String
	 * @return String 验证成功返回"",验证失败返回提示信息
	 */
	public static String checkNameLength(String name) {
		if (getTextLength(name) > 122) {
			// 不能超过122个字符或61个汉字
			return ParameterCheckInfoData.getNameLengthInfo();
		}
		return "";
	}

	/**
	 * 
	 * 判断给定参数是否超过1200个字符或600个汉字
	 * 
	 * @param note
	 *            String
	 * @return String 验证成功返回"",验证失败返回提示信息
	 */
	public static String checkNoteLength(String note) {
		if (getTextLength(note) > 1200) {
			// 不能超过1200个字符或600个汉字
			return ParameterCheckInfoData.getNoteLengthInfo();
		}
		return "";
	}

	/**
	 * 
	 * 判断名称是否合法
	 * 
	 * @param name
	 *            String
	 * @return boolean 含有
	 */
	public static boolean checkNameFileSpecialChar(String name) {
		if (Tool.isNullOrEmtryTrim(name)) {
			return false;
		} else {
			// 注册匹配模式(windows系统新建文件时不支持的特殊字符)
			Pattern pattern = Pattern
					.compile(ParameterCheckInfoData.matchSpecialChar);
			// 创建匹配给定参数与此模式的匹配器
			Matcher matcher = pattern.matcher(name);
			// 开始匹配
			return matcher.matches();
		}
	}

	/**
	 * 
	 * 获取给定字符串的字节长度，汉字为2个字节，字母数字等为一个字节
	 * 
	 * 字符串为null或""时返回0
	 * 
	 * @param text
	 *            字符串
	 * @return int 返回字符串的字节长度
	 */
	public static int getTextLength(String text) {
		try {
			return (text != null) ? text.getBytes("GBK").length : 0;
		} catch (UnsupportedEncodingException e) {
			return (text != null) ? text.getBytes().length : 0;
		}
	}

	

	/**
	 * 
	 * 校验文件名称是否满足要求,验证（名称不为空，字符正确）成功返回“”，否则返回相应提示信息
	 * 
	 * @param name
	 *            String 名称
	 * @return String 验证（名称不为空，字符正确，长度）成功返回""，否则返回相应提示信息
	 */
	public static String checkFileName(String name) {
		if (Tool.isNullOrEmtryTrim(name)) {
			// 名称不能为空
			return ParameterCheckInfoData.getNameNotNull();
		} else if (!checkNameFileSpecialChar(name)) {
			// 名称不能包括任意字符之一: \ / : * ? < > | # %
			return ParameterCheckInfoData.getNameErrorCharInfo();
		} else if (name.length() > 210) {
			// 名称长度不能超过121
			return ParameterCheckInfoData.getFileLengthInfo();
		}

		return "";
	}

	/**
	 * 
	 * 校验邮箱格式
	 * 
	 * @param mailAddr
	 *            String
	 * @return boolean 验证成功：true；验证失败：false
	 */
	public static boolean checkMailFormat(String mailAddr) {
		if (Tool.isNullOrEmtry(mailAddr)) {
			return false;
		}
		// 邮箱格式不正确
		Pattern emailer = Pattern.compile(ParameterCheckInfoData.EMAIL_FORMAT);
		if (!emailer.matcher(mailAddr).matches()) {
			return false;
		}
		return true;
	}
	
	/**
	* 验证输入密码长度 (6-18位)
	* 
	* @param 待验证的字符串
	* @return 如果是符合格式的字符串,返回 <b>true </b>,否则为 <b>false </b>
	*/
	public static boolean IsPasswLength(String str) {
	String regex = "^\\w{6,14}$";
	Pattern pattern = Pattern
			.compile(regex);
	// 创建匹配给定参数与此模式的匹配器
	Matcher matcher = pattern.matcher(str);
	// 开始匹配
	return matcher.matches();
	}
	/**
	* 验证电话号码
	* 
	* @param 待验证的字符串
	* @return 如果是符合格式的字符串,返回 <b>true </b>,否则为 <b>false </b>
	*/
	public static boolean IsTelephone(String str) {
//	String regex = "^\\w{1,20}$";
//	Pattern pattern = Pattern
//			.compile(regex);
//	// 创建匹配给定参数与此模式的匹配器
//	Matcher matcher = pattern.matcher(str);
//	// 开始匹配
	return true;
	}
}
