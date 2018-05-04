package com.mgp.common.utils;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpClientUtils {
    
   /**
    * @author mgp
    * @param url  获取数据的url
    * @param paraterMap  获取数据的map参数集合 Map<String,Object>
    * @return  获取的数据 String
    */
   public static String getSendRequest(String url, Map<String,Object> paraterMap){
	    Logger logger = LoggerFactory.getLogger(HttpClientUtils.class);
	    String temp="";
	    if(paraterMap!=null){
	    	url=url+"?";
	    	Iterator<String> paraterSetKey = paraterMap.keySet().iterator();
	    	for(int i=0;i<paraterMap.size();i++){
	    		String keyTemp=(String) paraterSetKey.next();
	    		url=url+keyTemp+"="+paraterMap.get(keyTemp)+"&";
	    	}
	    	url=url.substring(0, url.length()-1);
	    }
	    
	    //采用绕过验证的方式处理https请求  
	    SSLContext sslcontext = null;
		try {
			sslcontext = createIgnoreVerifySSL();
		} catch (KeyManagementException e1) {
			e1.printStackTrace();
		} catch (NoSuchAlgorithmException e1) {
			e1.printStackTrace();
		}  
	      
        // 设置协议http和https对应的处理socket链接工厂的对象  
        Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()  
           .register("http", PlainConnectionSocketFactory.INSTANCE)  
           .register("https", new SSLConnectionSocketFactory(sslcontext))  
           .build();  
        PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);  
        HttpClients.custom().setConnectionManager(connManager); 
        CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(connManager).build();  
	    RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(100000).setSocketTimeout(100000).setConnectionRequestTimeout(100000).build();
	    HttpGet httpGet = new HttpGet(url);
	    httpGet.setConfig(requestConfig);
	    httpGet.setHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/62.0.3202.62 Safari/537.36");
	    
	    CloseableHttpResponse response = null;
	    int cou=0;
	    try{        
	       do{
	    	   try{
	        	 response = httpClient.execute(httpGet);
	           }catch(org.apache.http.conn.ConnectTimeoutException connectTimeoutException){
	        	   CloseableHttpClient httpClient2 = HttpClients.custom().setConnectionManager(connManager).build();  
	        	   response = httpClient2.execute(httpGet);
	           }
	        	cou++;
	        }while((response.getStatusLine().getStatusCode()==403 || response.getStatusLine().getStatusCode()==443) && cou < 20);
	        if(response==null || response.getStatusLine().getStatusCode()!=200){
	        	logger.error("url："+url+"   获取失败！      date:  "+new Date()+" code:  "+response.getStatusLine().getStatusCode());
	        }
	    }catch (Exception e) {
	    	response = null;
	    	System.out.println(e);
	    	logger.error("http get request error!  url: "+url);
	    }   
	    try{
	    	if(response != null && response.getStatusLine().getStatusCode()==200){
	          HttpEntity entity = response.getEntity();
	          temp=EntityUtils.toString(entity,"UTF-8");
	        }else{
	        	temp=null;
	        }
	    }catch (Exception e) {
	    	temp=null;
	    	logger.error("转换数据出错！  url: "+url);
	    }finally{
	    	try {
	    		if(response!=null){
				  response.close();
				}
			} catch (IOException e) {
				logger.error("关闭  CloseableHttpResponse 出错！");
			}
	    	try {
	    		if(httpClient!=null){
				  httpClient.close();
				}
			} catch (IOException e) {
				logger.error("关闭  CloseableHttpClient 数据连接出错！");
			}   
	    } 
	    return temp;
   }
   
   public static String httpPostWithJSON(String url, String params) throws Exception {

       HttpPost httpPost = new HttpPost(url);
       CloseableHttpClient client = HttpClients.createDefault();
       String respContent = null;
       
//       json方式
       JSONObject jsonParam = new JSONObject(params);
       StringEntity entity = new StringEntity(jsonParam.toString(),"utf-8");//解决中文乱码问题    
       //entity.setContentEncoding("UTF-8");
       entity.setContentType("application/json");
       httpPost.setEntity(entity);
        
//       表单方式
//       List<BasicNameValuePair> pairList = new ArrayList<BasicNameValuePair>(); 
//       pairList.add(new BasicNameValuePair("name", "admin"));
//       pairList.add(new BasicNameValuePair("pass", "123456"));
//       httpPost.setEntity(new UrlEncodedFormEntity(pairList, "utf-8"));   
       
       
       HttpResponse resp = client.execute(httpPost);
       if(resp.getStatusLine().getStatusCode() == 200) {
           HttpEntity he = resp.getEntity();
           respContent = EntityUtils.toString(he,"UTF-8");
       }
       return respContent;
   }
   
   
   /** 
    * 绕过验证 
    *   
    * @return 
    * @throws NoSuchAlgorithmException  
    * @throws KeyManagementException  
    */  
   public static SSLContext createIgnoreVerifySSL() throws NoSuchAlgorithmException, KeyManagementException {  
       SSLContext sc = SSLContext.getInstance("SSLv3");  
     
       // 实现一个X509TrustManager接口，用于绕过验证，不用修改里面的方法  
       X509TrustManager trustManager = new X509TrustManager() {  
           @Override  
           public void checkClientTrusted(  
                   java.security.cert.X509Certificate[] paramArrayOfX509Certificate, String paramString) throws CertificateException {  
           }  
     
           @Override  
           public void checkServerTrusted(  
                   java.security.cert.X509Certificate[] paramArrayOfX509Certificate, String paramString) throws CertificateException {  
           }  
     
           @Override  
           public java.security.cert.X509Certificate[] getAcceptedIssuers() {  
               return null;  
           }  
       };  
     
       sc.init(null, new TrustManager[] { trustManager }, null);  
       return sc;  
   } 
}
