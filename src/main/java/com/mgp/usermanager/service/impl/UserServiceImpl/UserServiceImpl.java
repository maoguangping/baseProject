package com.mgp.usermanager.service.impl.UserServiceImpl;

import com.mgp.usermanager.dao.UserMapper;
import com.mgp.usermanager.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service("userService")
public class UserServiceImpl implements UserService {

    @Autowired(required = false)@Qualifier("userMapper")
    private UserMapper userDao;

    @Override
    public int deleteById(Long id) {
        return userDao.deleteByPrimaryKey(id);
    }
}
