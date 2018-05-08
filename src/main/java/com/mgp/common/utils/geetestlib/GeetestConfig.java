package com.yishenxiao.commons.utils.geetestlib;

import com.yishenxiao.commons.utils.PropertiesUtils;

/**
 * GeetestWeb配置文件
 * 
 *
 */
public class GeetestConfig {

	// 填入自己的captcha_id和private_key
	private static final String geetest_id = PropertiesUtils.getInfoConfigProperties().getProperty("geetestConfig.geetest_id");
	private static final String geetest_key = PropertiesUtils.getInfoConfigProperties().getProperty("geetestConfig.geetest_key");
	private static final boolean newfailback = true;

	public static final String getGeetest_id() {
		return geetest_id;
	}

	public static final String getGeetest_key() {
		return geetest_key;
	}
	
	public static final boolean isnewfailback() {
		return newfailback;
	}

}
