package com.yadong.doge.rpc.config;

import com.yadong.doge.registry.client.RegistryClient;
import com.yadong.doge.rpc.cluster.Cluster;
import com.yadong.doge.rpc.directory.AbstractDirectory;
import com.yadong.doge.rpc.directory.Directory;
import com.yadong.doge.rpc.directory.DynamicDirectory;
import com.yadong.doge.rpc.processor.DogeReferenceAnnotationScanProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
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
}
