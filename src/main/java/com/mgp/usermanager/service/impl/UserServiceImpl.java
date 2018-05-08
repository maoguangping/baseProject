package com.mgp.usermanager.service.impl;

import com.mgp.usermanager.bean.User;
import com.mgp.usermanager.dao.UserMapper;
import com.mgp.usermanager.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("userService")
public class UserServiceImpl implements UserService {
    @Autowired(required = false)@Qualifier("userDao")
    private UserMapper userDao;

    @Override
    public List<User> getListUser(String username) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("username", username);
        return userDao.queryByUserName(map);
    }
}
