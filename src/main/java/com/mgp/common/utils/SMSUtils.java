package com.yishenxiao.commons.utils;

import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yishenxiao.commons.beans.SMSBean;
  
public class SMSUtils{
	
	private static Logger logger = LoggerFactory.getLogger(SMSUtils.class);
	
	public static int sendSMS(HttpServletRequest req, SMSBean smsBean) {
		   String path = req.getSession().getServletContext().getRealPath("/")+"WEB-INF/classes/SMS.properties";
	       Properties properties = PropertiesUtils.getProperties(path); 
		   HttpClient client = new HttpClient(); 
		    PostMethod post = new PostMethod(properties.getProperty("user.urladdress")); 
		    post.addRequestHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");// 在头文件中设置转码 
		    NameValuePair[] data = { new NameValuePair("Uid", properties.getProperty("user.username")),//中国网建sms平台注册的用户名 
		        new NameValuePair("Key", properties.getProperty("user.key")),//中国网建sms平台注册的用户密钥 
		        new NameValuePair("smsMob", smsBean.getPhone()),//将要发送到的手机号码 
		        new NameValuePair("smsText", "您的验证码是 "+smsBean.getSmsText()) };//要发送的短信内容 
		    post.setRequestBody(data); 
		    int result = 0;
		    try{
			    client.executeMethod(post); 
			    /*Header[] headers = post.getResponseHeaders(); 
			    int statusCode = post.getStatusCode();
			    System.out.println("statusCode:" + statusCode); 
			    for (Header h : headers) { 
			      System.out.println(h.toString()); 
			    } */
			    String res = new String(post.getResponseBodyAsString().getBytes("utf-8")); 
			    result = Integer.parseInt(res);
			    if(result<=0){
			    	logger.error("短信发送失败！code: "+result);
			    }
		    }catch(Exception e){
		    	logger.error("短信发送失败！");
		    }finally{
		       post.releaseConnection(); 
		    }
		    return result;
		}
}