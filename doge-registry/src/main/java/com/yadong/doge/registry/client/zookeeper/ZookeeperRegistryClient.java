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
    public boolean registry(Method method, Class<?> interfaceClass, HostInfo info) {
        registry(method, interfaceClass.getName(), info);
        return true;
    }

    @Override
    public List<HostInfo> getHost(Method method,  Class<?> interfaceClass) {
        return getHost(method, interfaceClass.getName());
    }

    @Override
    public boolean registry(Method method, String interfaceName, HostInfo info) {
        logger.info("[Zookeeper] 发起注册, 方法:" + method + "###对象:" + interfaceName + "###(" + info.getHost() + ":" + info.getPort() + ")");
        String path = NameGenerateUtils.generateZkNodePath(method, interfaceName) + "/" + info.getHost() + ":" + info.getPort();
        zkCuratorUtil.createTempNodeSimple(path);
        zkCuratorUtil.setData(path, ObjectMapperUtils.toJSON(info.getHostData()));
        return true;
    }

    @Override
    public List<HostInfo> getHost(Method method, String interfaceName) {
        logger.info("[Zookeeper] 获取节点信息, 方法:" + method + "###对象:" + interfaceName);
        String parentNode = NameGenerateUtils.generateZkNodePath(method, interfaceName);
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


    @Override
    public List<HostInfo> getHost(String methodName, String interfaceName) {
        logger.info("[Zookeeper] 获取节点信息, 方法:" + methodName + "###对象:" + interfaceName);
        String parentNode = NameGenerateUtils.generateZkNodePath(methodName, interfaceName);
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

    @Override
    public HostData getHostData(Method method, String interfaceName, HostInfo hostInfo) {
        byte[] data = zkCuratorUtil.getData(NameGenerateUtils.generateZkNodePath(method, interfaceName) + "/" + hostInfo.getHostAndPort());
        String json = new String(data);
        return ObjectMapperUtils.toObject(json, HostData.class);
    }

    @Override
    public HostData getHostData(String key, HostInfo hostInfo) {
        byte[] data = zkCuratorUtil.getData(NameGenerateUtils.generateZkNodePathByKey(key) + "/" + hostInfo.getHostAndPort());
        String json = new String(data);
        return ObjectMapperUtils.toObject(json, HostData.class);
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
