package com.mgp.common.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.mgp.usermanager.beans.User;
import com.mgp.usermanager.service.UserService;

public class extensionUtils {
	
   public static Double getExtensionWallet(Long extensionUserId){
	   Properties properties = PropertiesUtils.getInfoConfigProperties();
	   Double extensionOne = Double.parseDouble(properties.getProperty("user.extension.one"));
	   Integer extensionCount = Integer.parseInt(properties.getProperty("user.extension.count"));
	   UserService userService = new extensionUtils().getUserServiceBean();
	   List<Long> userIds = new ArrayList<Long>();
	   Double extensionBase = extensionOne;
	   for(int i=1;i<=extensionCount;i++){
		   List<Long> userIdsTemp = new ArrayList<Long>();
		   if(userIds.size()>0){
		      userIdsTemp.addAll(userIds);
		   }
		   userIds = new ArrayList<Long>();
		   //查询父id为extensionUserId的用户
		   if(userIdsTemp.size()>0){
			   for(Long uid:userIdsTemp){
				   List<User> userList = userService.queryByExtensionUserId(uid);
				   extensionBase=extensionBase+getProfit(i, userList);
				   for(User user:userList){
					   userIds.add(user.getId());
				   }
			   }
		   }
		   if(i==1){
			   List<User> userList = userService.queryByExtensionUserId(extensionUserId);
			   extensionBase=extensionBase+getProfit(i, userList);
			   for(User user:userList){
				   userIds.add(user.getId());
			   }
		   }
	   }
	   return extensionBase;
   }
   
   public static Double getProfit(int i, List<User> userList){
	   Properties properties = PropertiesUtils.getInfoConfigProperties();
	   Double profit = 0.0;
	   Double extensionTemp = 0.0;
	   //计算利润
	   switch(i){
	       case 1:
	    	   extensionTemp = Double.parseDouble(properties.getProperty("user.extension.two"));
	    	   break;
	       case 2:
	    	   extensionTemp = Double.parseDouble(properties.getProperty("user.extension.three"));
	    	   break;
	       case 3:
	    	   extensionTemp = Double.parseDouble(properties.getProperty("user.extension.four"));
	    	   break;
	       case 4:
	    	   extensionTemp = Double.parseDouble(properties.getProperty("user.extension.five"));
	    	   break;
	       case 5:
	    	   extensionTemp = Double.parseDouble(properties.getProperty("user.extension.six"));
	    	   break;
	       case 6:
	    	   extensionTemp = Double.parseDouble(properties.getProperty("user.extension.seven"));
	    	   break;
	   }
	   profit = userList.size()*extensionTemp;
	   return profit;
   }
   
   public UserService getUserServiceBean(){
	   UserService userService = null;
	   if(userService==null){
		   userService = (UserService) SpringContextUtils.getBean("userService");
	   }
	   return userService;
   }
}
