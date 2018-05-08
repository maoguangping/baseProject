package com.yishenxiao.commons.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.yishenxiao.commons.beans.NativeSMS;
import com.yishenxiao.commons.beans.SmsVariableRequest;
import com.yishenxiao.commons.beans.SmsVariableResponse;

public class ChuangLanSmsUtil {

	private static Logger logger = LoggerFactory.getLogger(ChuangLanSmsUtil.class);

	public static String sendSmsByPost(String path, String postContent) {
		URL url = null;
		try {
			url = new URL(path);
			HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
			httpURLConnection.setRequestMethod("POST");
			httpURLConnection.setConnectTimeout(10000);
			httpURLConnection.setReadTimeout(2000);

			httpURLConnection.setDoOutput(true);
			httpURLConnection.setDoInput(true);
			httpURLConnection.setRequestProperty("Charset", "UTF-8");
			httpURLConnection.setRequestProperty("Content-Type", "application/json");

			httpURLConnection.connect();
			OutputStream os = httpURLConnection.getOutputStream();
			os.write(postContent.getBytes("UTF-8"));
			os.flush();

			StringBuilder sb = new StringBuilder();
			int httpRspCode = httpURLConnection.getResponseCode();
			if (httpRspCode == 200) {
				BufferedReader br = new BufferedReader(
						new InputStreamReader(httpURLConnection.getInputStream(), "utf-8"));
				String line = null;
				while ((line = br.readLine()) != null) {
					sb.append(line);
				}
				br.close();
				return sb.toString();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static int pushDomesticInfo(HttpServletRequest req, String phone, String phonecode, String nativecode) {
		String path = req.getSession().getServletContext().getRealPath("/") + "WEB-INF/classes/SMS.properties";
		Properties properties = PropertiesUtils.getProperties(path);
		if ("0086".equals(nativecode)) {
			// 中国
			String content = "【秒发键盘】您的验证码是：{$var}，短信有效时间为10分钟。";
			SmsVariableRequest smsVariableRequest = new SmsVariableRequest(properties.getProperty("user.cl253"),
					properties.getProperty("pwd.cl253"), content, phone + "," + phonecode, "true", null);
			String requestJson = JSON.toJSONString(smsVariableRequest);
			String response = ChuangLanSmsUtil.sendSmsByPost(properties.getProperty("url.cl253"), requestJson);
			SmsVariableResponse smsVariableResponse = (SmsVariableResponse) JSON.parseObject(response,
					SmsVariableResponse.class);
			return Integer.parseInt(smsVariableResponse.getCode());
		} else {
			String content = "【秒发键盘】您的验证码是：" + phonecode + "，短信有效时间为10分钟。";
			NativeSMS nativeSMS = new NativeSMS();
			nativeSMS.setAccount(properties.getProperty("user.nativecl253"));
			nativeSMS.setPassword(properties.getProperty("pwd.nativecl253"));
			nativeSMS.setMsg(content);
			nativeSMS.setMobile(nativecode + phone);
			SmsVariableResponse smsVariableResponse = null;
			try {
				String result = HttpUtil.post(properties.getProperty("url.nativecl253"), JsonUtils.toJson(nativeSMS));
				smsVariableResponse = (SmsVariableResponse) JSON.parseObject(result, SmsVariableResponse.class);
				return Integer.parseInt(smsVariableResponse.getCode());
			} catch (Exception e) {
				logger.info("phone:" + nativecode + phone + " " + "异常：" + e);
				return 1;
			}
		}
	}
}