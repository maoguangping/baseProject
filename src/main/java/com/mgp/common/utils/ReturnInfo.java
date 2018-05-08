package com.yishenxiao.commons.utils;

import java.util.HashMap;

/**
 * 
 * @author mgp
 * @info 后台返回信息的标准bean
 *
 */
public class ReturnInfo extends HashMap<String,Object>{

	private static final long serialVersionUID = 1L;
	public int code;
	public Object data;
	public Object msg;
	public int dataSize;
	public boolean databoolean;
	
	public ReturnInfo(){
		this.put("code", 200);
	}
	
	public ReturnInfo put(String key, Object value){
		super.put(key, value);
		return this;
	}
	
	public static ReturnInfo info(int code, Object data){
    	ReturnInfo info = new ReturnInfo();
    	info.put("code", code);
		info.put("data", data);	
		return info;
	}
	
    public static ReturnInfo info(int code, Object msg, int size){
    	ReturnInfo info = new ReturnInfo();
    	info.put("code", code);
    	info.put("data", code);
    	info.put("dataSize", size);
		info.put("msg", msg);
		return info;
	}
    
    public static ReturnInfo info(int code, Object data, boolean databoolean){
    	ReturnInfo info = new ReturnInfo();
    	info.put("code", code);
    	info.put("data", data);
    	info.put("databoolean", databoolean);
		return info;
	}
	
    public static ReturnInfo ok(){
		return new ReturnInfo();
	}
    
	@Override
	public String toString() {
		return "ReturnInfo [code=" + code + ", data=" + data + ", msg=" + msg + ", dataSize=" + dataSize
				+ ", databoolean=" + databoolean + "]";
	}

	public static ReturnInfo ok(Object msg){
		ReturnInfo info = new ReturnInfo();
	    info.put("data", msg);
		return info;
	}
	
	public static ReturnInfo error(){
		return error(500, "加载失败！");
	}
	
	public static ReturnInfo error(String msg){
		return error(500, msg);
	}
    
	public static ReturnInfo error(int code, String msg){
		ReturnInfo info = new ReturnInfo();
		info.put("code", code);
	    info.put("msg", msg);
		return info;
	}
	
}
