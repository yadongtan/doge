package com.yadong.doge.registry.client.zookeeper;

import com.yadong.doge.registry.config.HostData;
import com.yadong.doge.registry.config.HostInfo;
import com.yadong.doge.registry.client.RegistryClient;
import com.yadong.doge.registry.utils.ZkCuratorUtil;
import com.yadong.doge.utils.NameGenerateUtils;
import com.yadong.doge.utils.ObjectMapperUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

public class ZookeeperRegistryClient implements RegistryClient {

    private static final Logger logger = LoggerFactory.getLogger(ZookeeperRegistryClient.class);
    private final ZkCuratorUtil zkCuratorUtil;

    public ZookeeperRegistryClient(ZkCuratorUtil zkCuratorUtil){
        this.zkCuratorUtil = zkCuratorUtil;
    }

    @Override
    public boolean registry(Method method, Object obj, HostInfo info) {
        logger.info("[Zookeeper] 发起注册, 方法:" + method + "###对象:" + obj + "###(" + info.getHost() + ":" + info.getPort() + ")");
        String path = NameGenerateUtils.generateZkNodePath(method, obj) + "/" + info.getHost() + ":" + info.getPort();
        zkCuratorUtil.createNodeSimple(path);
        zkCuratorUtil.setData(path, ObjectMapperUtils.toJSON(info.getHostData()));
        return true;
    }

    @Override
    public List<HostInfo> getHost(Method method, Object obj) {
        logger.info("[Zookeeper] 获取节点信息, 方法:" + method + "###对象:" + obj);
        String parentNode = NameGenerateUtils.generateZkNodePath(method, obj);
        List<String> childNode = zkCuratorUtil.getChildNode(parentNode);
        // childNode.forEach(System.out::println);
        List<HostInfo> hostInfos = new LinkedList<>();
        for (String hi : childNode) {
            String path = parentNode + "/" + hi;
            HostInfo hostInfo = new HostInfo(hi.substring(0, hi.indexOf(":")), Integer.parseInt(hi.substring(hi.indexOf(":") + 1, hi.length())));
            HostData hostData = ObjectMapperUtils.toObject(new String(zkCuratorUtil.getData(path)), HostData.class);
            hostInfo.setHostData(hostData);
            hostInfos.add(hostInfo);
            logger.info("获取到节点信息:[" + hostInfo + "]");
        }
        return hostInfos;
    }

    @PostConstruct
    public void initInfo(){
        String path = "/doge/test1";
        String data = Long.toString(System.currentTimeMillis());
        logger.info("Zookeeper客户端开始测试-添加数据,路径:[" + path + "]-数据:[" +  data + "]");
        zkCuratorUtil.createNodeSimple(path);
        zkCuratorUtil.setData(path, data);
    }
}
