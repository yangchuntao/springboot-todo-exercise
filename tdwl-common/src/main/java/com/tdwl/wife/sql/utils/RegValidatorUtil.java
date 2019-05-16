package com.tdwl.wife.sql.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.regex.Pattern;

/**
 * reg表达式工具类,可自定义扩展
 */
public final class RegValidatorUtil {

//	private static final Pattern P_EMAL = Pattern.compile("^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$");
	
	private static final Pattern SIMPLE_PASSWORD = Pattern.compile("^[0-9_a-z_A-Z]{6,20}$");
	
/*	private static final Pattern PASSWORD = Pattern.compile("^(?![A-Z]+$)(?![a-z]+$)(?!\\d+$)(?![\\W_]+$)[^\u4e00-\u9fa5]\\S{5,17}$");
	private static final Pattern CENTER_PASSWORD = Pattern.compile("^(?=.*((?=[\\x21-\\x7e]+)[^A-Za-z0-9]))(?=.*[a-zA-Z])(?=.*[0-9])[^\u4e00-\u9fa5]{7,13}$");
	private static final Pattern STRONG_PASSWORD = Pattern.compile("^(?=.*((?=[\\x21-\\x7e]+)[^A-Za-z0-9]))(?=.*[a-zA-Z])(?=.*[0-9])[^\u4e00-\u9fa5]{13,17}$");*/
	
	//^1\d{10}$  ^((13[0-9])|(15[^4,\D])|(18[0,5-9]))\d{8}$
	//13开头的电话、15（第三位除去4）开头的电话、18（第三位除去1,2,3,4）开头的电话
	private static final Pattern P_MOBILEPHONE = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
	
	
//	public static boolean isEmail(String str) {
//		return StringUtils.isNotBlank(str) && P_EMAL.matcher(str).matches();
//	}
	
	public static boolean isSimplePassword(String str) {
		return StringUtils.isNotBlank(str) && SIMPLE_PASSWORD.matcher(str).matches();
	}
	
/*	public static boolean isPassword(String str) {
		return StringUtils.isNotBlank(str) && PASSWORD.matcher(str).matches();
	}
	
	public static boolean isCenterPassword(String str) {
		return StringUtils.isNotBlank(str) && CENTER_PASSWORD.matcher(str).matches();
	}

	public static boolean isStrongPassword(String str){
		return StringUtils.isNotBlank(str) && STRONG_PASSWORD.matcher(str).matches();
	}*/
	
	public static boolean isMobilephone(String str) {
		return StringUtils.isNotBlank(str) && P_MOBILEPHONE.matcher(str).matches();
	}
	
}
