package com.xyx.ls.util;

/**
 * 
 * 全局用户提示信息类
 * 
 * @author Administrator
 * 
 */
public class ParameterCheckInfoData {
	/** 禁用的特殊字符 */
	public static final String specialChar = "\\ / : * ? >< | # %";
	/** 用于匹配的特殊字符 */
	public static final String matchSpecialChar = "[^\\/\\:\\*\\?\\<\\>\\|\\#\\%\\\\]*";
	/** 邮箱格式 */
	public static final String EMAIL_FORMAT = "\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*";
	/** 文件名称长度不能超过211的字 */
	private static String fileLengthInfo = null;
	/** 不能超过122个字符或61个汉字 */
	private static String nameLengthInfo = null;
	/** 不能超过1200个字符或600个汉字 */
	private static String noteLengthInfo = null;
	/** 不能包括任意字符之一: \ / : * ? < > | # % */
	private static String nameErrorCharInfo = null;
	/** 不能为空 */
	private static String nameNotNull = null;
	/** 模板导出的方式 */
	private static boolean templateType = true;
	
	public static String getFileLengthInfo() {
		return fileLengthInfo;
	}

	public static void setFileLengthInfo(String fileLengthInfo) {
		ParameterCheckInfoData.fileLengthInfo = fileLengthInfo;
	}

	public static boolean isTemplateType() {
		return templateType;
	}

	public static void setTemplateType(boolean templateType) {
		ParameterCheckInfoData.templateType = templateType;
	}

	public static String getNameLengthInfo() {
		return nameLengthInfo;
	}

	public static void setNameLengthInfo(String nameLengthInfo) {
		ParameterCheckInfoData.nameLengthInfo = nameLengthInfo;
	}

	public static String getNoteLengthInfo() {
		return noteLengthInfo;
	}

	public static void setNoteLengthInfo(String noteLengthInfo) {
		ParameterCheckInfoData.noteLengthInfo = noteLengthInfo;
	}

	public static String getNameErrorCharInfo() {
		return nameErrorCharInfo;
	}

	public static void setNameErrorCharInfo(String nameErrorCharInfo) {
		ParameterCheckInfoData.nameErrorCharInfo = nameErrorCharInfo
				+ specialChar;
	}

	public static String getNameNotNull() {
		return nameNotNull;
	}

	public static void setNameNotNull(String nameNotNull) {
		ParameterCheckInfoData.nameNotNull = nameNotNull;
	}
}
