package com.yadong.doge.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.annotation.PostConstruct;

@ConfigurationProperties(prefix = "doge.zookeeper")
public class ZookeeperProperties {

    Logger logger = LoggerFactory.getLogger(ConsumerProperties.class);

    private String host;
    private int port;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    @PostConstruct
    public void initInfo(){
        logger.info("doge.zookeeper.host: " + host);
        logger.info("doge.zookeeper.port(s): " + port);
    }

}
