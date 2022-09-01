package com.yadong.doge.registry.client;

import com.yadong.doge.registry.config.HostInfo;

import java.lang.reflect.Method;
import java.util.List;

/**
* @author YadongTan
* @date 2022/8/30 10:50
* @Description 向注册中心注册的客户端
*/
public interface RegistryClient {
    public boolean registry(Method method, Class<?> interfaceClass, HostInfo info);
    public List<HostInfo> getHost(Method method, Class<?> interfaceClass);
    public boolean registry(Method method, String interfaceName, HostInfo info);
    public List<HostInfo> getHost(Method method, String interfaceName);
}
