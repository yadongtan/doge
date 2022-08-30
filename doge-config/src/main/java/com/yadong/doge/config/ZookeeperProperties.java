package com.yadong.doge.config;

import com.fasterxml.jackson.databind.exc.PropertyBindingException;
import com.yadong.doge.exception.PropertiesException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;

import javax.annotation.PostConstruct;
import javax.validation.constraints.NotNull;

@ConfigurationProperties(prefix = "doge.zookeeper")
public class ZookeeperProperties {

    Logger logger = LoggerFactory.getLogger(ConsumerProperties.class);

    private int connectionTimeout = 60000;
    private int sessionTimeout = 60000;
    private int retrySleepTime = 5000;
    private int maxRetries = 5;
    private String host;
    private int port = 2180;

    public Logger getLogger() {
        return logger;
    }

    public void setLogger(Logger logger) {
        this.logger = logger;
    }

    public int getConnectionTimeout() {
        return connectionTimeout;
    }

    public void setConnectionTimeout(int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    public int getSessionTimeout() {
        return sessionTimeout;
    }

    public void setSessionTimeout(int sessionTimeout) {
        this.sessionTimeout = sessionTimeout;
    }

    public int getRetrySleepTime() {
        return retrySleepTime;
    }

    public void setRetrySleepTime(int retrySleepTime) {
        this.retrySleepTime = retrySleepTime;
    }

    public int getMaxRetries() {
        return maxRetries;
    }

    public void setMaxRetries(int maxRetries) {
        this.maxRetries = maxRetries;
    }

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
        if(host == null){
            throw new PropertiesException("doge.zookeeper.host can not be null !");
        }
    }

}
