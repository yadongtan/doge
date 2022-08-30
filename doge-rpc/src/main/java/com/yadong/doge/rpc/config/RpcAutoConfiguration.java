package com.yadong.doge.rpc.config;

import com.yadong.doge.rpc.invoker.InvokersMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RpcAutoConfiguration {

    @Bean
    public InvokersMap invokersMap(){
        return new InvokersMap();
    }

}
