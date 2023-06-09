package com.yadong.doge.config.stater;

import com.yadong.doge.config.*;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
* @author YadongTan
* @date 2022/8/29 20:42
* @Description 加载配置信息
*/
@Configuration
public class DogeAutoConfigurationStarter {


    @Configuration
    @EnableConfigurationProperties(ConsumerProperties.class)
    protected static class ConsumerConfiguration{
    }

    @Configuration
    @EnableConfigurationProperties(ProviderProperties.class)
    protected static class ProviderConfiguration{

    }

    @Configuration
    @EnableConfigurationProperties(RedisProperties.class)
    protected static class RedisConfiguration{
    }

    @Configuration
    @EnableConfigurationProperties(MonitorProperties.class)
    protected static class MonitorConfiguration{
    }

    @Configuration
    @EnableConfigurationProperties(ZookeeperProperties.class)
    protected static class ZookeeperConfiguration{

//        @Bean("zookeeper")
//        public Object ZookeeperClient(ZookeeperProperties zookeeperProperties) {
//            return "ZookeeperClient";
//        }
    }

    @Configuration
    @EnableConfigurationProperties(RegistryProperties.class)
    protected static class RegistryConfiguration{

    }
}
