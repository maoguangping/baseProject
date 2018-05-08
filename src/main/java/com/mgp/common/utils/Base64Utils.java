package com.yishenxiao.commons.utils;

import java.io.UnsupportedEncodingException;

import org.apache.commons.codec.binary.Base64;

public class Base64Utils {
    public static String base64encode(String message) {  
        try {  
            byte[] encodeBase64 = Base64.encodeBase64(message.getBytes("UTF-8")); 
            return new String(encodeBase64);  
        } catch (UnsupportedEncodingException e) {  
            throw new RuntimeException();  
        }  
  
    }  
      
    public static String base64decode(String message) {  
        byte[] encodeBase64 = Base64.decodeBase64(message);  
        return new String(encodeBase64);   
    }   
    
    public static String base64encodeByte(byte[] b) {  
        byte[] encodeBase64 = Base64.encodeBase64(b);  
        return new String(encodeBase64);   
    }   
    
    public static String base64decodeByte(byte[] b) {  
        byte[] encodeBase64 = Base64.decodeBase64(b);  
        return new String(encodeBase64);   
    }
}