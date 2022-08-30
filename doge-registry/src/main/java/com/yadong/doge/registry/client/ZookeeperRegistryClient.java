package com.yadong.doge.registry.client;

import com.yadong.doge.registry.utils.ZkCuratorUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import java.lang.reflect.Method;

public class ZookeeperRegistryClient implements RegistryClient{

    private static final Logger logger = LoggerFactory.getLogger(ZookeeperRegistryClient.class);
    private ZkCuratorUtil zkCuratorUtil;

    public ZookeeperRegistryClient(ZkCuratorUtil zkCuratorUtil){
        this.zkCuratorUtil = zkCuratorUtil;
    }

    @Override
    public boolean registry(Method method, Object obj, HostInfo info) {
        logger.info("[Zookeeper] 发起注册, 方法:" + method + "###对象:" + obj + "###(" + info.getHost() + ":" + info.getPort() + ")");
        return true;
    }

    @Override
    public HostInfo getHost(Method method, Object obj) {
        return null;
    }

    @PostConstruct
    public void initInfo(){
        String path = "/doge/test1";
        String data = Long.toString(System.currentTimeMillis());
        logger.info("Redis客户端开始测试-添加数据,路径:[" + path + "]-数据:[" +  data + "]");
        zkCuratorUtil.createNodeSimple(path);
        zkCuratorUtil.setData(path, data);
    }
}
