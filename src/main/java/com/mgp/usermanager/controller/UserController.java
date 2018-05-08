package com.mgp.usermanager.controller;

import com.mgp.common.dao.MongoDao;
import com.mgp.common.dao.RedisDao;
import com.mgp.usermanager.bean.User;
import com.mgp.usermanager.dao.UserMapper;
import com.mgp.usermanager.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("user")
public class UserController {
    @Autowired(required = false)@Qualifier("userService")
    private UserService userService;
    @Autowired(required = false)@Qualifier("redisDao")
    private RedisDao redisDao;
    @Autowired(required = false)@Qualifier("mongoDao")
    private MongoDao mongoDao;

    private Logger logger = LoggerFactory.getLogger(UserController.class);

    @GetMapping("getUser/{username}")
    public List<User> getUser(@PathVariable("username") String username){
        /*try{
            int c=1/0;
        }catch(Exception e){
            logger.error(e.toString());
        }*/
        //int c=1/0;
        redisDao.setKey("b1","bb1");
        System.out.println("test: "+redisDao.getValue("b1"));
        System.out.println("mongo: "+mongoDao.getData("jiepaiCollection"));
        return userService.getListUser(username);
    }
}
