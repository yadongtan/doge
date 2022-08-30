package com.yadong.doge.registry.client;

import java.lang.reflect.Method;
import java.util.List;

/**
* @author YadongTan
* @date 2022/8/30 10:50
* @Description 向注册中心注册的客户端
*/
public interface RegistryClient {
    public boolean registry(Method method, Object obj, HostInfo info);
    public List<HostInfo> getHost(Method method, Object obj);
}
