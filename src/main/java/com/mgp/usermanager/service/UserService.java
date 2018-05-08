package com.mgp.usermanager.service;

import com.mgp.usermanager.bean.User;

import java.util.List;

public interface UserService {
    public List<User> getListUser(String username);
}
