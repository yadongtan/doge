package com.yadong.doge.config.stater;

import com.yadong.doge.config.ConsumerProperties;
import com.yadong.doge.config.ProviderProperties;
import com.yadong.doge.config.RedisProperties;
import com.yadong.doge.config.ZookeeperProperties;
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
    @EnableConfigurationProperties(ZookeeperProperties.class)
    protected static class ZookeeperConfiguration{

        @Bean("zookeeper")
        public Object ZookeeperClient(ZookeeperProperties zookeeperProperties) {
            return "ZookeeperClient";
        }
    }
}
