package com.yadong.doge.rpc.directory;


import com.yadong.doge.registry.config.HostInfo;
import com.yadong.doge.rpc.invoker.Invoker;

import java.lang.reflect.Method;
import java.util.List;

/**
* @author YadongTan
* @date 2022/8/31 19:27
* @Description 从注册中心拉取集群信息, 实时监控提供者变化并更新提供者信息
*/
public interface Directory {

    //List<HostInfo> pull(Method method, String interfaceName);
    public List<HostInfo> pull(Method method, Class<?> targetClass);
    List<HostInfo> search(Invoker invoker);
}
