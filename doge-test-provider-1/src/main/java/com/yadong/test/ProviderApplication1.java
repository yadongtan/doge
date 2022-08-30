package com.yadong.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.redis.core.RedisTemplate;

@SpringBootApplication
public class ProviderApplication1 {


    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(ProviderApplication1.class, args);

    }
}
