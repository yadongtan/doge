package com.yadong.doge.rpc.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.yadong.doge.config.ConsumerProperties;
import com.yadong.doge.registry.client.RegistryClient;
import com.yadong.doge.registry.config.HostData;
import com.yadong.doge.registry.config.HostInfo;
import com.yadong.doge.rpc.directory.AbstractDirectory;
import com.yadong.doge.rpc.directory.DirectoryCuratorCacheListener;
import com.yadong.doge.rpc.directory.DynamicDirectory;
import com.yadong.doge.rpc.monitor.MonitorSender;
import com.yadong.doge.rpc.processor.DogeReferenceAnnotationScanProcessor;
import com.yadong.doge.utils.NameGenerateUtils;
import com.yadong.doge.utils.ObjectMapperUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.api.CuratorEvent;
import org.apache.curator.framework.api.CuratorListener;
import org.apache.curator.framework.recipes.cache.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(
        proxyBeanMethods = false
)
public class RpcAutoConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(RpcAutoConfiguration.class);

    @Bean
    public DogeReferenceAnnotationScanProcessor dogeReferenceAnnotationScanProcessor(){
        return new DogeReferenceAnnotationScanProcessor();
    }


    @Configuration
    protected static class RegistryClientAutoConfiguration{


        @ConditionalOnBean(CuratorFramework.class)
        @Bean
        DynamicDirectory dynamicDirectory(RegistryClient registryClient, CuratorFramework curatorFramework) {
            CuratorCache nodeCache = CuratorCache.build(curatorFramework, NameGenerateUtils.ZOOKEEPER_PATH_PREFIX);
            nodeCache.listenable().addListener(new DirectoryCuratorCacheListener());
            nodeCache.start();
            AbstractDirectory.setRegistryClient(registryClient);
            return DynamicDirectory.getInstance();
        }


        @ConditionalOnMissingBean(CuratorFramework.class)
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
