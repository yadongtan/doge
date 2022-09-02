package com.yadong.doge.monitor.controller;

import com.yadong.doge.monitor.map.MonitorMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MonitorCenterController {


    @RequestMapping(value = "/monitor", method = RequestMethod.GET)
    public String getMonitorMapInfo(){
        return MonitorMap.show();
    }

}
