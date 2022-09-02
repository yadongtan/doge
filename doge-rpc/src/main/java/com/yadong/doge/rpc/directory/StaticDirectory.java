package com.yadong.doge.rpc.directory;


import com.yadong.doge.registry.config.HostInfo;
import com.yadong.doge.rpc.invoker.Invoker;
import com.yadong.doge.utils.NameGenerateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.List;

/**
* @author YadongTan
* @date 2022/9/1 1:20
* @Description 暂时先不实现
*/
public class StaticDirectory  extends AbstractDirectory{

    private static final Logger logger = LoggerFactory.getLogger(AbstractDirectory.class);

    @Override
    public List<HostInfo> pull(Method method, Class<?> targetClass) {
        List<HostInfo> newList = registryClient.getHost(method, targetClass);
        List<HostInfo> oldList = hostInfoMap.put(NameGenerateUtils.generateMethodMapKey(method, targetClass), newList);
        if (oldList != null && (oldList.containsAll(newList) && oldList.size() != newList.size())) {
            logger.info("更新服务提供者主机目录");
            StringBuilder builder = new StringBuilder();
            for (HostInfo hostInfo : oldList) {
                builder.append(hostInfo).append("/");
            }
            logger.info("原服务提供者者主机目录:[" + builder + "]");
            StringBuilder builder1 = new StringBuilder();
            for (HostInfo hostInfo : newList) {
                builder1.append(hostInfo).append("/");
            }
            logger.info("现服务提供者主机目录:[" + builder1 + "]");
        }
        return newList;
    }

    @Override
    public List<HostInfo> search(Invoker invoker) {
        List<HostInfo> hostInfos = hostInfoMap.get(invoker.getKey());
        if(hostInfos == null || hostInfos.isEmpty()){
            return pull(invoker.getMethod(), invoker.getTargetClass());
        }
        return null;
    }

    @Override
    public void refresh(String key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void refresh(String key, HostInfo hostInfo) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void addHostInfo(String key, HostInfo hostInfo) {
        throw new UnsupportedOperationException();
    }

}
