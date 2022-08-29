package com.yadong.test.service.impl;

import com.yadong.doge.rpc.annotation.DogeService;
import entity.User;
import service.UserService;

@DogeService
public class UserServiceImpl implements UserService {

    @Override
    public User getUserInfo(String username) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(Long.toString(System.currentTimeMillis()));
        return user;
    }

    @Override
    public String loginUser(String username, String password) {
        return "用户["+ username + "]登录成功";
    }
}
