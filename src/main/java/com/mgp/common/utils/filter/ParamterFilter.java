package com.yishenxiao.commons.utils.filter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yishenxiao.commons.utils.PropertiesUtils;

public class ParamterFilter implements Filter {

	private static Logger logger = LoggerFactory.getLogger(ParamterFilter.class);
	private String asepawd=PropertiesUtils.getInfoConfigProperties().getProperty("ase.pawd");
	private String excludedUrl;
	private String[] excludedUrls;
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		excludedUrl = filterConfig.getInitParameter("excludedUrls");
		if (excludedUrl != null && excludedUrl.length() > 0) {
			excludedUrls = excludedUrl.split(",");
		}
	}
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		boolean flag = false;
		for (String uri : excludedUrls) {
			if (uri.equals(req.getServletPath())) {
				flag = true;
				break;
			}
		}
		if (flag) {
			chain.doFilter(request, response);
		} else {
			try {
				// 解析post的json参数
				String requestBody = getBody((HttpServletRequest) request);
				if(StringUtils.isNotBlank(requestBody)){
					JSONObject both = new JSONObject(requestBody);
					String dataCode = both.getString("both");
					// 使用解析数据重新生成ServletRequest，供doChain调用
					request = getRequest(request, requestBody);
					try {
					    String requestBody1= AESUtil.decryptAES(dataCode, asepawd);
						request = getRequest(request, requestBody1);
						chain.doFilter(request, response);
					} catch (Exception e) {
						logger.error("requestBody: "+requestBody);
						return;
					}
				}else{
					chain.doFilter(request, response);
				}
			} catch (JSONException e) {
				logger.error(getBody((HttpServletRequest) request)+" json转换失败！");
			}
		}
	}

	@Override
	public void destroy() {
	}
	
	private String getBody(HttpServletRequest request) throws IOException {
		String body = null;
		StringBuilder stringBuilder = new StringBuilder();
		BufferedReader bufferedReader = null;
		try {
			InputStream inputStream = request.getInputStream();
			if (inputStream != null) {
				bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
				char[] charBuffer = new char[128];
				int bytesRead = -1;
				while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
					stringBuilder.append(charBuffer, 0, bytesRead);
				}
			} else {
				stringBuilder.append("");
			}
		} catch (IOException ex) {
			
		} finally {
			if (null != bufferedReader) {
				bufferedReader.close();
			}
		}
		body = stringBuilder.toString();
		return body;
	}

	/**
	 * 将post解析过后的request进行封装改写
	 * 
	 * @param request
	 * @param body
	 * @return
	 */
	private ServletRequest getRequest(ServletRequest request, String body) {
		String enctype = request.getContentType();
		if (StringUtils.isNotEmpty(enctype) && enctype.contains("application/json")) {
			return new PostServletRequest((HttpServletRequest) request, body);
		}
		return request;
	}	

}
