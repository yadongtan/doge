package com.yadong.doge.rpc.directory;


import com.yadong.doge.registry.config.HostInfo;
import com.yadong.doge.utils.NameGenerateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.List;

/**
* @author YadongTan
* @date 2022/9/1 1:20
* @Description Static Directory pull from registry center
 *             which means, the directory once pull from it, the host list will never change
*/
public class StaticDirectory  extends AbstractDirectory{

    private static final Logger logger = LoggerFactory.getLogger(AbstractDirectory.class);

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
                builder1.append(hostInfo).append("/");
            }
            logger.info("现服务提供者主机目录:[" + builder1 + "]");
        }
    }

}
