/*package com.yishenxiao.commons.utils.filter;

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
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;

import com.yishenxiao.commons.utils.SpringContextUtils;
import com.yishenxiao.usermanager.beans.User;
import com.yishenxiao.usermanager.service.UserService;

public class TokenFilter implements Filter {

	private String excludedUrl;
	private String[] excludedUrls;
	private static Logger logger = LoggerFactory.getLogger(TokenFilter.class);

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
		HttpServletResponse resp = (HttpServletResponse) response;
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
				// 使用解析数据重新生成ServletRequest，供doChain调用
				request = getRequest(request, requestBody);
				if (StringUtils.isNotEmpty(requestBody)) {
					if (requestBody.startsWith("{")) {
						JSONObject jsonData = new JSONObject(requestBody);
						String token = (String) jsonData.get("usertoken");
						// 根据token获取用户id和用户名称，可以扩展增加其他请求参数
						if (StringUtils.isNotEmpty(token)) {
							@SuppressWarnings("unchecked")
							RedisTemplate<String, Object> redisService = (RedisTemplate<String, Object>) SpringContextUtils
									.getBean("redisTemplate");
							String userId = (String) redisService.opsForValue().get(token);
							if (StringUtils.isNotEmpty(userId)) {
								UserService userService = (UserService) SpringContextUtils.getBean("userService");
								User user = userService.queryByUserId(Long.parseLong(userId));
								if (user != null) {
									if (!jsonData.has("userName")) {
										jsonData.put("userName", user.getUsername());
									}
									if (!jsonData.has("userId")) {
										jsonData.put("userId", userId);
									}
									flag = true;
									request = getRequest(request, jsonData.toString());
									chain.doFilter(request, response);
								}
							}
						}
					}
				}
				if (!flag) {
					//logger.error("requestBody: " + requestBody);
					resp.setHeader("Access-Control-Allow-Origin", "*");
					resp.setHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
					resp.setHeader("Access-Control-Allow-Headers",
							"Origin, No-Cache, X-Requested-With, If-Modified-Since, Pragma, Last-Modified, Cache-Control, Expires, Content-Type, X-E4M-With");
					return;
				}
			} catch (JSONException e) {
				logger.error(getBody((HttpServletRequest) request) + " json转换失败！");
			}
		}
	}

	public static String hexStringToString(String s) {
		if (s == null || s.equals("")) {
			return null;
		}
		s = s.replace(" ", "");
		byte[] baKeyword = new byte[s.length() / 2];
		for (int i = 0; i < baKeyword.length; i++) {
			try {
				baKeyword[i] = (byte) (0xff & Integer.parseInt(s.substring(i * 2, i * 2 + 2), 16));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		try {
			s = new String(baKeyword, "UTF-8");
			new String();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return s;
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

	*//**
	 * 将post解析过后的request进行封装改写
	 * 
	 * @param request
	 * @param body
	 * @return
	 *//*
	private ServletRequest getRequest(ServletRequest request, String body) {
		String enctype = request.getContentType();
		if (StringUtils.isNotEmpty(enctype) && enctype.contains("application/json")) {
			return new PostServletRequest((HttpServletRequest) request, body);
		}
		return request;
	}

}
*/