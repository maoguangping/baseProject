package com.yishenxiao.commons.utils;

public class StringUtils {
	
   /**
    * @author mgp
    * @info  String字段进行判空的方法
    * @param str
    * @return true 为空       false为非空
    */
   public static boolean judgeBlank(String str){
	   if(str==null || str.equals("")){
		   return true;
	   }else{
		   return false;
	   }
   }
}
