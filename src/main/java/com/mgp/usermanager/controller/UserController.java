package com.mgp.usermanager.controller;

import com.mgp.usermanager.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class UserController {

    @Autowired(required = false)@Qualifier("userService")
    private UserService userService;

    @RequestMapping("getObject")
    public Map<String,Object> getObject(){
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("1",2);
        //userService.deleteById(1L);
        return map;
    }
}
