package com.mgp.common.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.mgp.commons.utils.StringUtils;

public class DateUtils {
	 private static Logger logger = LoggerFactory.getLogger(DateUtils.class);
	 /**
	  * @author mgp
	  * @return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
	  */
	 private static SimpleDateFormat getSimpleDateFormat(){
		 return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
	 }
	 /**
	  * @author mgp
	  * @return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.S")
	  */
	 private static SimpleDateFormat getOtherSimpleDateFormat(){
		 return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.S");
	 }
	 /**
	  * @author mgp
	  * @return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
	  */
	 private static SimpleDateFormat getSimpleDate(){
		 return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	 }
	 /**
	  * @author mgp
	  * @param String
	  * @info  字符串转时间    yyyy-MM-dd'T'HH:mm:ss.S  yyyy-MM-dd'T'HH:mm:ss
	  */
     public static Date parse(String dateString){
    	 if(StringUtils.judgeBlank(dateString)){
    		 return null;
    	 }
    	 SimpleDateFormat simpleDateFormat = null;
    	 if(dateString.contains(".")){
    		 simpleDateFormat = getOtherSimpleDateFormat();
    	 }else if(dateString.contains("T")){
    		 simpleDateFormat = getSimpleDateFormat();
    	 }else{
    		 simpleDateFormat = getSimpleDate();
    	 }
    			 
    	 Date date = null;
		try {
			date = simpleDateFormat.parse(dateString);
		} catch (ParseException e) {
			logger.error("DateUtils String 转  Date类型转换失败！");
		}
    	 return date;
     }
     
     /**
	  * @author mgp
	  * @param date
	  * @info  时间 转字符串    yyyy-MM-dd HH:mm:ss
	  */
     public static String formatString(Date date){
    	 if(date==null){
    		 return null;
    	 }
    	 SimpleDateFormat simpleDateFormat = getSimpleDate();
    	 String dateString=null;
		try {
			dateString = simpleDateFormat.format(date);
		} catch (Exception e) {
			logger.error("DateUtils Date 转  String类型转换失败！");
		}
    	 return dateString;
     }
     
     
     /**
	  * @author mgp
	  * @param date
	  * @info  时间 转字符串   yyyy-MM-dd'T'HH:mm:ss
	  */
     public static String formatT(Date date){
    	 if(date == null){
    		 return null;
    	 }
    	 SimpleDateFormat simpleDateFormat = getSimpleDateFormat();
    	 String dateString = simpleDateFormat.format(date);  	 
    	 return dateString;
     }
     
     /**
	  * @author mgp
	  * @param date
	  * @info  时间 转字符串   yyyy-MM-dd'T'HH:mm:ss.S
	  */
     public static String formatTS(Date date){
    	 if(date == null){
    		 return null;
    	 }
    	 SimpleDateFormat simpleDateFormat = getOtherSimpleDateFormat();
    	 String dateString = simpleDateFormat.format(date);  	 
    	 return dateString;
     }
     
  /*   public static void main(Stirng[] args){
    	 
     }*/
}
