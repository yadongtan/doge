package com.yadong.test.controller;

import com.yadong.doge.rpc.annotation.DogeReference;
import com.yadong.doge.rpc.loadbalance.LoadBalanceFactory;
import com.yadong.doge.rpc.status.RpcStatus;
import entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import service.UserService;


@RestController
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @DogeReference(loadBalance = LoadBalanceFactory.ROUND)
    UserService userService;

    @RequestMapping("/info/{username}")
    public User getUserInfo(@PathVariable("username")String username){
        for(int i = 0;i < 30; i++) {
            int finalI = i;
            new Thread(() -> {
                userService.getUserInfo("[ " + Integer.toString(finalI) + "]" + username);
            }).start();
        }
        return userService.getUserInfo(username);
    }

    @RequestMapping("/login/{username}/{password}")
    public String loginUser(@PathVariable("username")String username,
                            @PathVariable("password")String password){

        return userService.loginUser(username, password);
    }


    @RequestMapping("/monitor")
    public String monitor(){
        return RpcStatus.show();
    }

//    @RequestMapping("/test")
//    public String loginUser() throws InterruptedException {
//        logger.info(Long.toString(System.currentTimeMillis()));
//        Thread.sleep(20000);
//        return "Test";
//    }

}
