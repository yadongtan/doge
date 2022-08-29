package com.yadong.test.controller;

import entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import service.UserService;

@RestController
public class UserController {

    @Autowired
    UserService userService;

    @RequestMapping("/info/{username}")
    public User getUserInfo(@PathVariable("username")String username){
        return userService.getUserInfo(username);
    }

    @RequestMapping("/login/{username}/{password}")
    public String loginUser(@PathVariable("username")String username,
                            @PathVariable("password")String password){
        return userService.loginUser(username, password);
    }

}
