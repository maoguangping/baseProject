package com.yishenxiao.commons.utils;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.UnauthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;

/**
 * 异常处理器
 * @info 服务器异常处理
 * @author mgp
 */
@Component
public class MyExceptionHandler implements HandlerExceptionResolver {
	private Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	public ModelAndView resolveException(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex) {
		ReturnInfo r = new ReturnInfo();
		ModelAndView mv = new ModelAndView(); ;
		try {
			response.setContentType("application/json;charset=utf-8");
			response.setCharacterEncoding("utf-8");
			if (ex instanceof org.apache.catalina.connector.ClientAbortException) {
				r = ReturnInfo.error(500, "请不要频繁刷新！");
			} else if (ex instanceof org.apache.shiro.authz.AuthorizationException) {
			} else if (ex instanceof UnauthorizedException) {
				mv.setViewName("redirect:../login.html");  
	            return mv;  
			} else if (ex instanceof RRException) {
				r.put("code", ((RRException) ex).getCode());
				r.put("msg", ((RRException) ex).getMessage());
				logger.info(ex.getMessage(), ex);
			} else if (ex instanceof DuplicateKeyException) {
				r = ReturnInfo.error("数据库中已存在该记录！");
				logger.info(ex.getMessage(), ex);
			} else if (ex instanceof AuthorizationException) {
				//r = ReturnInfo.error("没有权限，请联系管理员授权！");
				logger.info(ex.getMessage(), ex);
				mv.setViewName("redirect:../login.html");  
				mv.addObject("msg", "没有权限，请联系管理员授权！");
	            return mv;  
			} else if (ex instanceof java.lang.IllegalStateException) {
				System.out.println("IllegalStateException异常，未知错误！");
				logger.info(ex.getMessage(), ex);
			} else if (ex instanceof net.sf.json.JSONException) {
				System.out.println("JSONException异常，可能是域名解析失败！");
				logger.info(ex.getMessage(), ex);
			} else if (ex instanceof org.apache.shiro.session.InvalidSessionException) {
				System.out.println("InvalidSessionException异常：session已经注销！");
				logger.info(ex.getMessage(), ex);
			} else if (ex instanceof com.mysql.jdbc.exceptions.jdbc4.CommunicationsException) {
				System.out.println("CommunicationsException异常：数据库连接失败！");
				logger.info(ex.getMessage(), ex);
			} else if (ex instanceof javax.servlet.ServletException) {
				r = ReturnInfo.error(404, "请求的资源找不到！");
			} else {
				r = ReturnInfo.error();
				// 记录异常日志
				logger.error(ex.getMessage(), ex);
			}
			String json = JSON.toJSONString(r);
			try {
				PrintWriter pw = response.getWriter();
				pw.print(json);
				pw.close();
			} catch (Exception e) {
				// 此异常是io重复调用异常，换outputStream会报Not an ISO 8859-1 character:
				// 未，暂时不处理
				// logger.info(e.getMessage(), e);
			}
		} catch (Exception e) {
			logger.error("RRExceptionHandler 异常处理失败", e);
		}
		return new ModelAndView();
	}
}
