package com.yadong.doge.registry.client.redis;

import com.yadong.doge.registry.config.HostInfo;
import com.yadong.doge.registry.client.RegistryClient;
import com.yadong.doge.registry.utils.RedisUtil;
import com.yadong.doge.utils.NameGenerateUtils;
import com.yadong.doge.utils.ObjectMapperUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class RedisRegistryClient implements RegistryClient {

    private static final Logger logger = LoggerFactory.getLogger(RedisRegistryClient.class);

    private final RedisUtil redisUtil;

    public RedisRegistryClient(RedisUtil redisUtil){
        this.redisUtil = redisUtil;
    }

    @Override
    public boolean registry(Method method, Class<?> interfaceClass, HostInfo info) {
        return registry(method, interfaceClass.getName(), info);
    }



    @Override
    public List<HostInfo> getHost(Method method, Class<?> interfaceClass) {
        return getHost(method, interfaceClass.getName());
    }

    @Override
    public boolean registry(Method method, String interfaceName, HostInfo info) {
        logger.info("[Redis] 发起注册, 方法:" + method + "###对象:" + interfaceName + "###(" + info.getHost() + ":" + info.getPort() + ")");
        redisUtil.lSet(NameGenerateUtils.generateRedisPath(method, interfaceName), ObjectMapperUtils.toJSON(info));
        return true;
    }

    @Override
    public List<HostInfo> getHost(Method method, String interfaceName) {
        logger.info("[Redis] 拉取主机信息, 方法:" + method + "###对象:" + interfaceName );
        List<Object> list = redisUtil.lGet(NameGenerateUtils.generateRedisPath(method, interfaceName), 0, -1);
        if(list == null){
            return null;
        }
        List<HostInfo> hostInfos = new ArrayList<>(list.size());
        for (Object hi : list) {
            HostInfo hostInfo = ObjectMapperUtils.toObject(hi.toString(), HostInfo.class);
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
