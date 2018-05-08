package com.yishenxiao.commons.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

public class AliyunRealNameAuthentication {
	
	private static String host = "https://dm-51.data.aliyun.com";
    private static String path = "/rest/160601/ocr/ocr_idcard.json";
    private static String method = "POST";
    private static String appcode = PropertiesUtils.getInfoConfigProperties().getProperty("appcode");
    private static Logger logger = Logger.getLogger(AliyunRealNameAuthentication.class);
    
   public static Map<String,Object> getIdentityRealNameAuthentication(List<String> strList){
	   Map<String,Object> map = new HashMap<String,Object>();
	   String baseStrFace = BaseUrlUtils.getImageStr(strList.get(0));
		if(baseStrFace.equals("fail")){
			map.put("code", 0);
			return map;
		}
		Map<String, String> querys = new HashMap<String, String>();
		String bodys = "{\"image\":\""+baseStrFace+"\",\"configure\":{\"side\":\"face\"}}";// #身份证正反面类型: face/back
		String result = getIdentityRealNameAuthenticationFaceInfo(bodys, querys);
		if(result!=null){
			map.put("code",1);
			map.put("resultface", result);
		}else{
			map.put("code", 0);
			return map;
		}
		String baseStrBack = BaseUrlUtils.getImageStr(strList.get(1));
		if(baseStrBack.equals("fail")){
			map.put("code", 0);
			return map;
		}
		bodys = "{\"image\":\""+baseStrBack+"\",\"configure\":{\"side\":\"back\"}}";// #身份证正反面类型: face/back
		result = getIdentityRealNameAuthenticationFaceInfo(bodys, querys);
        if(result!=null){
        	map.put("code",1);
			map.put("resultback", result);
		}else{
			map.put("code", 0);
			return map;
		}
	return map;	   
   }
   
   private static String getIdentityRealNameAuthenticationFaceInfo(String bodys,Map<String,String> querys){
	   String temp=null;
	    try {
	    	/**
	    	* 重要提示如下:
	    	* HttpUtils请从
	    	* https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/src/main/java/com/aliyun/api/gateway/demo/util/HttpUtils.java
	    	* 下载
	    	*
	    	* 相应的依赖请参照
	    	* https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/pom.xml
	    	*/
	    	HttpResponse response = HttpUtils.doPost(host, path, method, getHeaders(), querys, bodys);
	    	HttpEntity entity = response.getEntity();
	    	temp=EntityUtils.toString(entity,"UTF-8");
	    	if(response != null && response.getStatusLine().getStatusCode()==200){
	    		return temp;
	        }else{
	        	logger.error("Ali cloud identity authentication interface "+temp);
	        	temp=null;
	        }
	    } catch (Exception e) {
	    	logger.error("Abnormity in the Ali cloud identity authentication interface");
	    }
	    return temp;
   }
   
   
   private static Map<String, String> getHeaders(){
	    Map<String, String> headers = new HashMap<String, String>();
	    //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
	    headers.put("Authorization", "APPCODE " + appcode);
	    //根据API的要求，定义相对应的Content-Type
	    headers.put("Content-Type", "application/json; charset=UTF-8");
	    return headers;
   }
}
