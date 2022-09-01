package com.yadong.doge.rpc.directory;

import com.yadong.doge.registry.config.HostInfo;
import com.yadong.doge.utils.NameGenerateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.List;

/**
 * @author YadongTan
 * @date 2022/9/1 1:19
 * @Description dynamic directory pull from registry center
 */
public class DynamicDirectory extends AbstractDirectory {

    private static final Logger logger = LoggerFactory.getLogger(DynamicDirectory.class);

    @Override
    public void pull(Method method, Object object) {
        List<HostInfo> newList = registryClient.getHost(method, object);
        List<HostInfo> oldList = hostInfoMap.put(NameGenerateUtils.generateMethodMapKey(method, object), newList);
        if (oldList != null && (oldList.containsAll(newList) && oldList.size() != newList.size())) {
            logger.info("更新服务提供者主机目录");
            StringBuilder builder = new StringBuilder();
            for (HostInfo hostInfo : oldList) {
                builder.append(hostInfo).append("/");
            }
            logger.info("原服务提供者者主机目录:[" + builder + "]");
            StringBuilder builder1 = new StringBuilder();
            for (HostInfo hostInfo : newList) {
                builder.append(hostInfo).append("/");
            }
            logger.info("现服务提供者主机目录:[" + builder1 + "]");
        }
    }

}
