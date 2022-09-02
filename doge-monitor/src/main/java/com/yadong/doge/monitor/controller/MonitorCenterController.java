package com.yadong.doge.monitor.controller;

import com.yadong.doge.monitor.map.MonitorMap;
import com.yadong.doge.registry.status.RpcStatus;
import com.yadong.doge.rpc.directory.DynamicDirectory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MonitorCenterController {


    @RequestMapping(value = "/monitor", method = RequestMethod.GET)
    public String getMonitorMapInfo(){
        return MonitorMap.show();
    }

    @RequestMapping(value = "/hostmap", method = RequestMethod.GET)
    public String getHostMap(){
        return DynamicDirectory.getInstance().show();
    }
//    @RequestMapping("/test")
//    public String loginUser() throws InterruptedException {
//        logger.info(Long.toString(System.currentTimeMillis()));
//        Thread.sleep(20000);
//        return "Test";
//    }

}
