package com.yadong.doge.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.annotation.PostConstruct;

/**
* @author YadongTan
* @date 2022/8/29 20:12
* @Description 调用远程服务需要的一些配置信息
*/
@ConfigurationProperties(prefix = "doge.remote")
public class ConsumerProperties {
    Logger logger = LoggerFactory.getLogger(ConsumerProperties.class);


    @PostConstruct
    public void initInfo(){
    }

}
