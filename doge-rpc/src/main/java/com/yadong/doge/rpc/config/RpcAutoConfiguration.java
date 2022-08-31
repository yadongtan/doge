package com.yadong.doge.rpc.config;

import com.yadong.doge.rpc.processor.DogeReferenceAnnotationScanProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(
        proxyBeanMethods = false
)
public class RpcAutoConfiguration {

    @Bean
    public DogeReferenceAnnotationScanProcessor dogeReferenceAnnotationScanProcessor(){
        return new DogeReferenceAnnotationScanProcessor();
    }

}
