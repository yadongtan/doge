package com.yadong.doge.rpc.config;

import com.yadong.doge.config.ConsumerProperties;
import com.yadong.doge.registry.client.RegistryClient;
import com.yadong.doge.rpc.directory.AbstractDirectory;
import com.yadong.doge.rpc.directory.DynamicDirectory;
import com.yadong.doge.rpc.monitor.MonitorSender;
import com.yadong.doge.rpc.processor.DogeReferenceAnnotationScanProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
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


    @Configuration
    protected static class RegistryClientAutoConfiguration{

        @Bean
        DynamicDirectory dynamicDirectory(RegistryClient registryClient){
            AbstractDirectory.setRegistryClient(registryClient);
            return DynamicDirectory.getInstance();
        }
    }

    @Configuration
    protected static class MonitorNettyClientAutoConfiguration{

        //开始连接doge监控中心的netty客户端
        @ConditionalOnProperty(prefix = "doge.monitor",name = "enable", havingValue = "true", matchIfMissing = false)
        @Bean
        MonitorSender monitorNettyClient(ConsumerProperties properties) {
            //创建实例并启动netty服务器去连接monitor中心
            return MonitorSender.createInstance(properties);
        }
    }

}
