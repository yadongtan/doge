package com.yadong.doge.rpc.directory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.yadong.doge.registry.config.HostData;
import com.yadong.doge.registry.config.HostInfo;
import com.yadong.doge.utils.NameGenerateUtils;
import com.yadong.doge.utils.ObjectMapperUtils;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.CuratorCacheListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
* @author YadongTan
* @date 2022/9/3 0:16
* @Description 完成服务的动态发现与注册
*/
public class DirectoryCuratorCacheListener implements CuratorCacheListener {

    private static final Logger logger = LoggerFactory.getLogger(DirectoryCuratorCacheListener.class);

    @Override
    public void event(Type type, ChildData oldData, ChildData data) {
        System.out.println("/....");
        System.out.println("/....");
        if (oldData != null) {
            logger.info("oldData = " + ObjectMapperUtils.toJSON(oldData));
        }
        if (data != null) {
            logger.info("data = " + ObjectMapperUtils.toJSON(data));
        }
        // TODO: 2022/9/2 实现事件调度
        switch (type) {
            case NODE_CREATED:
                logger.info("创建节点事件");
                if (data != null) {
                    logger.info("path: " + data.getPath());
                    logger.info("data: " + new String(data.getData()));
                    String hostDataString = new String(data.getData());
                    if (!hostDataString.isEmpty()) {
                        //有新数据添加了
                        HostData hostData = null;
                        try {
                            hostData = ObjectMapperUtils.toObjectWithException(hostDataString, HostData.class);
                        } catch (JsonProcessingException e) {
                            // 转换失败说明不是HostData被添加了,不管它
                            return;
                        }
                        String hostAndPort = NameGenerateUtils.getHostAndPortFromZkNodePath(data.getPath());
                        HostInfo hostInfo = new HostInfo(hostAndPort, hostData);
                        String key = NameGenerateUtils.generateKeyByZkNodePath(data.getPath());
                        DynamicDirectory.getInstance().addHostInfo(key, hostInfo);
                    }
                }
                break;
            case NODE_CHANGED:
                logger.info("节点更新事件");
                if (data != null) {
                    logger.info("path: " + data.getPath());
                    logger.info("data: " + new String(data.getData()));
                    String hostDataString = new String(data.getData());
                    if (!hostDataString.isEmpty()) {
                        //有新数据添加了
                        HostData hostData = null;
                        try {
                            hostData = ObjectMapperUtils.toObjectWithException(hostDataString, HostData.class);
                        } catch (JsonProcessingException e) {
                            // 转换失败说明不是HostData被添加了,不管它
                            return;
                        }
                        String hostAndPort = NameGenerateUtils.getHostAndPortFromZkNodePath(data.getPath());
                        HostInfo hostInfo = new HostInfo(hostAndPort, hostData);
                        String key = NameGenerateUtils.generateKeyByZkNodePath(data.getPath());
                        DynamicDirectory.getInstance().addHostInfo(key, hostInfo);
                    }
                }
                break;
            case NODE_DELETED:
                logger.info("节点删除");
                if (oldData != null) {
                    data = oldData;
                    logger.info("path: " + data.getPath());
                    logger.info("data: " + new String(data.getData()));
                    String hostDataString = new String(data.getData());
                    if (!hostDataString.isEmpty()) {
                        //有新数据添加了
                        HostData hostData = null;
                        try {
                            hostData = ObjectMapperUtils.toObjectWithException(hostDataString, HostData.class);
                        } catch (JsonProcessingException e) {
                            // 转换失败说明不是HostData被添加了,不管它
                            return;
                        }
                        String hostAndPort = NameGenerateUtils.getHostAndPortFromZkNodePath(data.getPath());
                        HostInfo hostInfo = new HostInfo(hostAndPort, hostData);
                        String key = NameGenerateUtils.generateKeyByZkNodePath(data.getPath());
                        DynamicDirectory.getInstance().deleteFromLocal(key, hostInfo);
                    }
                }
                break;
        }
    }
}
