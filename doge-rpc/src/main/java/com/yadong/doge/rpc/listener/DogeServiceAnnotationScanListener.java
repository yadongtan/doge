package com.yadong.doge.rpc.listener;


import com.yadong.doge.config.ProviderProperties;
import com.yadong.doge.registry.config.HostInfo;
import com.yadong.doge.registry.client.RegistryClient;
import com.yadong.doge.rpc.annotation.DogeService;
import com.yadong.doge.rpc.invoker.RpcMethodObjectMap;
import com.yadong.doge.rpc.netty.provider.NettyRpcServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;

/**
* @author YadongTan
* @date 2022/8/30 10:19
* @Description 扫描所有标注了@DogeService的类, 生成Map
*/
public class DogeServiceAnnotationScanListener implements ApplicationListener<ContextRefreshedEvent> {

    Logger logger = LoggerFactory.getLogger(DogeServiceAnnotationScanListener.class);

    public DogeServiceAnnotationScanListener(){

    }


    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        //扫描
        ApplicationContext applicationContext = contextRefreshedEvent.getApplicationContext();
        Map<String, Object> beansWithAnnotation = applicationContext.getBeansWithAnnotation(DogeService.class);
        if(beansWithAnnotation.isEmpty()){
            return;         //如果没有要暴露的服务,那么不用执行后面的操作了
        }
        RegistryClient registryClient = applicationContext.getBean(RegistryClient.class);
        HostInfo hostInfo = null;
        try {
            hostInfo = new HostInfo(InetAddress.getLocalHost().getHostAddress(),
                    applicationContext.getBean(ProviderProperties.class).getPort());
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        //新线程,注册可能比较耗时, 不影响主线程继续执行Spring
        HostInfo info = hostInfo;
        new Thread(() -> {
            if(!beansWithAnnotation.isEmpty()){
                NettyRpcServer.getInstance().start(applicationContext.getBean(ProviderProperties.class));
            }
            beansWithAnnotation.forEach((key, obj) ->{
                RpcMethodObjectMap.getInstance().addInvokerMethod(obj);
                for (Method method : obj.getClass().getDeclaredMethods()) {
                    registryClient.registry(method, obj.getClass().getInterfaces()[0], info);
                    registryClient.getHost(method, obj.getClass().getInterfaces()[0]);
                }
            });
        }).start();
    }

}
