package com.yishenxiao.commons.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.yishenxiao.commons.utils.StringUtils;

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
	  * @info  时间 转字符串    yyyy-MM-dd
	  */
     public static Date formatDate(Date date){
    	 if(date==null){
    		 return null;
    	 }
    	 SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    	 Date date01=null;
		try {
			String dateString = simpleDateFormat.format(date);
			date01 = simpleDateFormat.parse(dateString);
		} catch (Exception e) {
			logger.error("DateUtils Date 转  String类型转换失败！");
		}
    	 return date01;
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
     
     /**
	  * @author mgp
	  * @param date
	  * @info  时间毫秒 转时间  yyyy-MM-dd HH:mm:ss
	  */
     public static Date longformatDate(String strDate){
    	Calendar calendar = Calendar.getInstance();
    	calendar.setTimeInMillis(Long.valueOf(strDate));
    	Date date = calendar.getTime();
    	SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	String strD = simpleDateFormat.format(date);
    	try {
			date = simpleDateFormat.parse(strD);
		} catch (ParseException e) {
			logger.error("string 转  time fail"+e);
		}
    	 return date;
     }
     
     /**
	  * @author mgp
	  * @param date
	  * @info  时间 转字符串   yyyy-MM-dd HH:mm:ss
	  */
     public static Date formatDate(String strDate){
    	SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	Date date= null;
		try {
			date = simpleDateFormat.parse(strDate);
		} catch (ParseException e) {
			logger.error("string 转  time fail"+e);
		}
    	 return date;
     }
     
     /**
	  * @author mgp
	  * @param type 1代表北京，2代表东京， 3代表伦敦
	  * @info  得到不同时区的时间
	  */
     private static Date getDifferentTimeZoneTime(int type){
    	 Date date = new Date();
    	 SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	 if(type==1){
    	   simpleDateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));  // 设置北京时区  
    	 }else if(type==2){
    	   simpleDateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Tokyo"));  // 设置东京时区  
    	 }else if(type==3){
    	   simpleDateFormat.setTimeZone(TimeZone.getTimeZone("Europe/London"));  // 设置伦敦时区 
    	 }
    	 String tempTimeStr=simpleDateFormat.format(date);
    	 Date tokyoDate=new Date();
		try {
			tokyoDate = simpleDateFormat.parse(tempTimeStr);
		} catch (ParseException e) {
		   logger.error("时间解析失败");	
		}
    	 return tokyoDate;
     }
     
     /**
	  * @author mgp
	  * @param type 1代表北京，2代表东京， 3代表伦敦
	  * @info  得到不同时区的时间
	  */
     private static String getDifferentTimeZoneTimeString(int type){
    	 Date date = new Date();
    	 SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	 if(type==1){
    	   simpleDateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));  // 设置北京时区  
    	 }else if(type==2){
    	   simpleDateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Tokyo"));  // 设置东京时区  
    	 }else if(type==3){
    	   simpleDateFormat.setTimeZone(TimeZone.getTimeZone("Europe/London"));  // 设置伦敦时区 
    	 }
    	 return simpleDateFormat.format(date);
     }
     
     /**
	  * @author mgp
	  * @param 
	  * @info  根据设置的时区获得时间，type 1代表北京，2代表东京， 3代表伦敦
	  */
     public static Date getNowTime(){
    	 int timeType=Integer.parseInt(PropertiesUtils.getInfoConfigProperties().getProperty("timeZone"));
    	 return getDifferentTimeZoneTime(timeType);
     }
     
     
     /**
	  * @author mgp
	  * @param 
	  * @info  根据设置的时区获得时间，type 1代表北京，2代表东京， 3代表伦敦
	  */
     public static String getNowTimeStr(){
    	 int timeType=Integer.parseInt(PropertiesUtils.getInfoConfigProperties().getProperty("timeZone"));
    	 return getDifferentTimeZoneTimeString(timeType);
     }
     /*
     public static void main(Stirng[] args){
    	 
     }*/
}
