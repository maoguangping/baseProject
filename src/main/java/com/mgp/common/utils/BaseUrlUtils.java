package com.yishenxiao.commons.utils;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BaseUrlUtils {

	private static Logger logger = LoggerFactory.getLogger(BaseUrlUtils.class);
	
	/**
	 * @Description: 将base64编码字符串转换为图片
	 * @Author: 
	 * @CreateTime: 
	 * @param imgStr base64编码字符串
	 * @param path 图片路径-具体到文件
	 * @return
	*/
	public static boolean generateImage(String imgStr, String path) {
		if (imgStr == null){
		   return false;
		}
		try {
			// 解密
			byte[] b = Base64.decodeBase64(imgStr.getBytes("UTF-8"));		
		    // 处理数据
			for (int i = 0; i < b.length; ++i) {
			if (b[i] < 0) {
			b[i] += 256;
			}
			}
			OutputStream out = new FileOutputStream(path);
			out.write(b);
			out.flush();
			out.close();
			return true;
		} catch (Exception e) {
			logger.error("Save picture error");
		   return false;
		}
	}

	/**
	 * @Description: 根据图片地址转换为base64编码字符串
	 * @Author:
	 * @CreateTime:
	 * @return
	 * @throws Exception 
	 */
	public static String getImageStr(String imgFile){
		byte[] data = getInputStreamByGet(imgFile);
		if(data==null){
			return "fail";
		}		
		// 加密
		return new String(Base64.encodeBase64(data));
	}
	
	 public static byte[] getInputStreamByGet(String urltemp){
		byte[] data=null;
        try {
        	URL url = new URL(urltemp);  
            //打开链接  
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();  
            //设置请求方式为"GET"  
            conn.setRequestMethod("GET");  
            //超时响应时间为5秒  
            conn.setConnectTimeout(10 * 1000);  
            //通过输入流获取图片数据  
            InputStream inStream = conn.getInputStream();  
            //得到图片的二进制数据，以二进制封装得到数据，具有通用性  
            data = readInputStream(inStream);  
            return data;

        } catch (Exception e) {
        	logger.error("Error reading and writing ID pictures");
        }
        return data;
    }
	 
	 public static byte[] readInputStream(InputStream inStream) throws Exception{  
	        ByteArrayOutputStream outStream = new ByteArrayOutputStream();  
	        //创建一个Buffer字符串  
	        byte[] buffer = new byte[1024];  
	        //每次读取的字符串长度，如果为-1，代表全部读取完毕  
	        int len = 0;  
	        //使用一个输入流从buffer里把数据读取出来  
	        while( (len=inStream.read(buffer)) != -1 ){  
	            //用输出流往buffer里写入数据，中间参数代表从哪个位置开始读，len代表读取的长度  
	            outStream.write(buffer, 0, len);  
	        }  
	        //关闭输入流  
	        inStream.close();  
	        //把outStream里的数据写入内存  
	        return outStream.toByteArray();  
	    } 

}
