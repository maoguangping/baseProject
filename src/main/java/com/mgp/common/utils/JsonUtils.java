package com.yishenxiao.commons.utils;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;



public class JsonUtils {
	
	/**
	 * fromJsonToObject<br>
	 * jackjson把json字符串转换为Java对象的实现方法
	 * 
	 * <pre>
	 * return Jackson.jsonToObj(this.answersJson, new TypeReference&lt;List&lt;StanzaAnswer&gt;&gt;() {
	 * });
	 * </pre>
	 * 
	 * @param <T>
	 *            转换为的java对象
	 * @param json
	 *            json字符串
	 * @param typeReference
	 *            jackjson自定义的类型
	 * @return 返回Java对象
	 */
	public static <T> T jsonToObj(String json, TypeReference<T> typeReference) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.readValue(json, typeReference);
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	

	/**
	 * fromJsonToObject<br>
	 * json转换为java对象
	 * 
	 * <pre>
	 * return Jackson.jsonToObj(this.answersJson, Jackson.class);
	 * </pre>
	 * 
	 * @param <T>
	 *            要转换的对象
	 * @param json
	 *            字符串
	 * @param valueType
	 *            对象的class
	 * @return 返回对象
	 */
	public static <T> T jsonToObj(String json, Class<T> valueType) {
		ObjectMapper mapper = new ObjectMapper();
		//mapper.configure(org.codehaus.jackson.map.DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);  
		try {
			return mapper.readValue(json, valueType);
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * fromObjectToJson<br>
	 * java对象转换为json字符串
	 * 
	 * @param object
	 *            Java对象
	 * @return 返回字符串
	 */
	public static String toJson(Object object) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.writeValueAsString(object);
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * fromObjectToJson<br>
	 * java对象转换为json字符串
	 * 
	 * @param object
	 *            要转换的对象
	 * @param filterName
	 *            过滤器的名称
	 * @param properties
	 *            要过滤哪些字段
	 * @return
	 */
/*	@SuppressWarnings("deprecation")
	public static String objToJson(Object object, String filterName, Set<String> properties) {
		ObjectMapper mapper = new ObjectMapper();
		FilterProvider filters = new SimpleFilterProvider().addFilter(filterName, SimpleBeanPropertyFilter.serializeAllExcept(properties));
		try {
			return mapper.filteredWriter(filters).writeValueAsString(object);
		} catch (JsonGenerationException e) {
			logger.error("JsonGenerationException: ", e);
		} catch (JsonMappingException e) {
			logger.error("JsonMappingException: ", e);
		} catch (IOException e) {
			logger.error("IOException: ", e);
		}
		return null;
	}*/
}
