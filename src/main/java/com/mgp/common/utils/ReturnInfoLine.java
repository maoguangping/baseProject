package com.yishenxiao.commons.utils;

import java.util.LinkedHashMap;

/**
 * 
 * @author mgp
 * @info 后台返回信息的标准bean
 *
 */
public class ReturnInfoLine extends LinkedHashMap<String,Object>{

	private static final long serialVersionUID = 1L;
	public boolean success;
	public Object msg;
	public int msgSize;
	
	
	public ReturnInfoLine(){
		this.put("code", 200);
	}
	
	public ReturnInfoLine(boolean b){
		this.put("success", b);
	}
	
	public ReturnInfoLine put(String key, Object value){
		super.put(key, value);
		return this;
	}
	
	public static ReturnInfoLine info(int code, Object msg){
    	ReturnInfoLine info = new ReturnInfoLine();
    	info.put("code", code);
		info.put("msg", msg);	
		return info;
	}
	
	public static ReturnInfoLine info(boolean b, Object msg){
    	ReturnInfoLine info = new ReturnInfoLine(b);
		info.put("data", msg);	
		return info;
	}
	
    public static ReturnInfoLine info(int code, Object msg, int size){
    	ReturnInfoLine info = new ReturnInfoLine();
    	info.put("code", code);
    	info.put("msgSize", size);
		info.put("msg", msg);	
		return info;
	}
	
    public static ReturnInfoLine ok(){
		return new ReturnInfoLine();
	}
    
	public static ReturnInfoLine ok(String msg){
		ReturnInfoLine info = new ReturnInfoLine();
	    info.put("msg", msg);
		return info;
	}
	
	public static ReturnInfoLine error(){
		return error(500, "加载失败！");
	}
	
	public static ReturnInfoLine error(String msg){
		return error(500, msg);
	}
    
	public static ReturnInfoLine error(int code, String msg){
		ReturnInfoLine info = new ReturnInfoLine();
		info.put("code", code);
	    info.put("msg", msg);
		return info;
	}
	
}
