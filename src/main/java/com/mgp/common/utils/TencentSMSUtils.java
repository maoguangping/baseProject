package com.yishenxiao.commons.utils;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yishenxiao.commons.beans.TencentSMSBean;
import com.yishenxiao.commons.utils.qcloudsms.SmsMultiSender;
import com.yishenxiao.commons.utils.qcloudsms.SmsMultiSenderResult;
import com.yishenxiao.commons.utils.qcloudsms.SmsSingleSender;
import com.yishenxiao.commons.utils.qcloudsms.SmsSingleSenderResult;

public class TencentSMSUtils {
	
   private static Logger logger = LoggerFactory.getLogger(TencentSMSUtils.class);	
   
   //初始化单发
   private static SmsSingleSender getSmsSingleSender(HttpServletRequest req, TencentSMSBean tencentSMSBean) throws NumberFormatException, Exception{
	   return new SmsSingleSender(tencentSMSBean.getAppid(), tencentSMSBean.getAppkey());
   }
   //初始化群发
   private static SmsMultiSender getSmsMultiSender(HttpServletRequest req, TencentSMSBean tencentSMSBean) throws NumberFormatException, Exception{
	   return new SmsMultiSender(tencentSMSBean.getAppid(), tencentSMSBean.getAppkey());
   }
   //模板单发
   public static SmsSingleSenderResult getSmsSingleSenderResult(HttpServletRequest req, TencentSMSBean tencentSMSBean){
	   SmsSingleSenderResult smsSingleSenderResult = null;
	   try{
		   SmsSingleSender singleSender = getSmsSingleSender(req, tencentSMSBean);
		   smsSingleSenderResult = singleSender.sendWithParam(tencentSMSBean.getNationCode(), tencentSMSBean.getPhoneNumberList().get(0),
				                                               tencentSMSBean.getTemplId(), tencentSMSBean.getParams(), tencentSMSBean.getSign(),
				                                                tencentSMSBean.getExtend(), tencentSMSBean.getExt());
	   }catch(Exception e){
		   logger.error("模板单发短信失败！");
	   }
	   return smsSingleSenderResult;
   }
   
   
   //模板群发
   public static SmsMultiSenderResult getSmsMultiSenderResult(HttpServletRequest req, TencentSMSBean tencentSMSBean){
	   SmsMultiSenderResult smsMultiSenderResult = null;
	   try{
		   SmsMultiSender multiSender = getSmsMultiSender(req, tencentSMSBean);
		   smsMultiSenderResult = multiSender.sendWithParam(tencentSMSBean.getNationCode(), tencentSMSBean.getPhoneNumberList(),
			                                               tencentSMSBean.getTemplId(), tencentSMSBean.getParams(), tencentSMSBean.getSign(),
			                                                tencentSMSBean.getExtend(), tencentSMSBean.getExt());
	   }catch(Exception e){
		   logger.error("模板单发短信失败！");
	   }
	   return smsMultiSenderResult;
   }
}
