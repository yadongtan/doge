package com.yadong.doge.rpc.directory;


import java.lang.reflect.Method;

/**
* @author YadongTan
* @date 2022/8/31 19:27
* @Description 从注册中心拉取集群信息, 实时监控提供者变化并更新提供者信息
*/
public interface Directory {

    void pull(Method method, Object object);


}
