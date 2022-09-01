package com.yadong.test.impl;

import com.yadong.doge.config.ProviderProperties;
import com.yadong.doge.rpc.annotation.DogeService;
import entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import service.UserService;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Properties;

@DogeService
public class UserServiceImpl implements UserService {


    @Autowired
    ProviderProperties properties;

    @Override
    public User getUserInfo(String username) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(Long.toString(System.currentTimeMillis()));
        return user;
    }

    @Override
    public String loginUser(String username, String password) {
        try {
            return InetAddress.getLocalHost().getHostAddress()+ ":" + properties.getPort() +"]用户[" + username + "]登录成功";
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return null;
    }
}
