package com.yadong.doge.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "doge")
public class RegistryProperties {
    //注册中心名字,zookeeper和redis,默认zookeeper
    private String registry;

    public String getRegistry() {
        return registry;
    }

    public void setRegistry(String registry) {
        this.registry = registry;
    }
}
