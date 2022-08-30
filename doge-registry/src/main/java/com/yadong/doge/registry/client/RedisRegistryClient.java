package com.yadong.doge.registry.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yadong.doge.registry.utils.RedisUtil;
import com.yadong.doge.utils.NameGenerateUtils;
import com.yadong.doge.utils.ObjectMapperUtil;
import org.apache.curator.framework.CuratorFramework;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class RedisRegistryClient implements RegistryClient{

    private static final Logger logger = LoggerFactory.getLogger(RedisRegistryClient.class);

    private final RedisUtil redisUtil;

    public RedisRegistryClient(RedisUtil redisUtil){
        this.redisUtil = redisUtil;
    }

    @Override
    public boolean registry(Method method, Object obj, HostInfo info) {
        logger.info("[Redis] 发起注册, 方法:" + method + "###对象:" + obj + "###(" + info.getHost() + ":" + info.getPort() + ")");
        redisUtil.lSet(NameGenerateUtils.generateRedisPath(method, obj), ObjectMapperUtil.toJSON(info));
        return true;
    }

    @Override
    public List<HostInfo> getHost(Method method, Object obj) {
        logger.info("[Redis] 拉取主机信息, 方法:" + method + "###对象:" + obj );
        List<Object> list = redisUtil.lGet(NameGenerateUtils.generateRedisPath(method, obj), 0, -1);
        if(list == null){
            return null;
        }
        List<HostInfo> hostInfos = new ArrayList<>(list.size());
        for (Object hi : list) {
            HostInfo hostInfo = ObjectMapperUtil.toObject(hi.toString(), HostInfo.class);
                hostInfos.add(hostInfo);
        }
        for (HostInfo hostInfo : hostInfos) {
            logger.info("[Redis]获取到主机信息:" + hostInfo);
        }
        return hostInfos;
    }

    @PostConstruct
    public void initInfo(){
        String path = "/doge/test1";
        String data =  Long.toString(System.currentTimeMillis());
        logger.info("Zookeeper客户端开始测试-添加数据,路径:[" + path + "]-数据:[" +  data + "]");
        redisUtil.lSet(path,data);
    }
}
