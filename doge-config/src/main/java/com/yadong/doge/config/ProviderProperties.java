package com.yadong.doge.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.annotation.PostConstruct;

/**
* @author YadongTan
* @date 2022/8/29 20:12
* @Description 服务暴露需要的一些参数配置
*/
@ConfigurationProperties(prefix = "doge.server")
public class ProviderProperties {

    Logger logger = LoggerFactory.getLogger(ProviderProperties.class);

    private int port;

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    @PostConstruct
    public void initInfo(){
        logger.info("doge.server.port(s): " + port);
    }

}
