package com.yadong.doge.registry.client;

import com.yadong.doge.registry.utils.RedisUtil;
import org.apache.curator.framework.CuratorFramework;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import java.lang.reflect.Method;

public class RedisRegistryClient implements RegistryClient{

    private static final Logger logger = LoggerFactory.getLogger(RedisRegistryClient.class);

    private RedisUtil redisUtil;

    public RedisRegistryClient(RedisUtil redisUtil){
        this.redisUtil = redisUtil;
    }

    @Override
    public boolean registry(Method method, Object obj, HostInfo info) {
        logger.info("[Redis] 发起注册, 方法:" + method + "###对象:" + obj + "###(" + info.getHost() + ":" + info.getPort() + ")");
        return true;
    }

    @Override
    public HostInfo getHost(Method method, Object obj) {
        return null;
    }

    @PostConstruct
    public void initInfo(){
        String path = "/doge/test1";
        String data =  Long.toString(System.currentTimeMillis());
        logger.info("Zookeeper客户端开始测试-添加数据,路径:[" + path + "]-数据:[" +  data + "]");
        redisUtil.lSet(path,data);
    }
}
