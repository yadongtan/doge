package com.yadong.doge.monitor.config;

import com.yadong.doge.config.MonitorProperties;
import com.yadong.doge.monitor.netty.MonitorNettyServer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MonitorNettyServerConfig {

    @Bean
    MonitorNettyServer monitorNettyServer(MonitorProperties properties){
        MonitorNettyServer.getInstance().syncStart(properties);
        return MonitorNettyServer.getInstance();
    }

}
