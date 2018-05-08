package com.yishenxiao.commons.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PropertiesUtils {
	
	private static Logger logger = LoggerFactory.getLogger(PropertiesUtils.class);
	
    public static Properties getProperties(String path){
    	Properties properties = new Properties();
	       InputStream in = null;
	       try {
	            in = new FileInputStream(new File(path)); 
	            properties.load(in);
			} catch (IOException e) {
				logger.error("读取公司邮箱失败！");
			}finally{
	           if (in!=null) {
	               try {
	            	   in.close();
				} catch (IOException e) {
					logger.error("关闭  io流 失败!");
				}
	           }
	       }
	       return properties;
    }
    
    public static Properties getInfoConfigProperties(){   	
    	InputStream inputStream = PropertiesUtils.class.getClassLoader().getResourceAsStream("InfoConfig.properties");
    	Properties properties = new Properties();
        try {
        	properties.load(inputStream);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }finally{
           if (inputStream!=null) {
               try {
            	   inputStream.close();
			} catch (IOException e) {
				logger.error("关闭  io流 失败!");
			}
           }
       }
	       return properties;
    }
}
