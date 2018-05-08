package com.mgp.common.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 系统页面视图
 * 
 * @author 毛广平
 * @info 页面访问处理类
 * @date 2017年07月24日
 */
@Controller
@RequestMapping("page")
public class SysPageController {
	

	@RequestMapping("{fileName}")
	public String page(@PathVariable("fileName") String fileName,String catalog){
		
		if(catalog == null) {
			return fileName+".html";
		}
		return  catalog+"/"+fileName+".html" ;

	}
	
	
}
