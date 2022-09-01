package com.yadong.doge.rpc.loadbalance;

import com.yadong.doge.registry.config.HostInfo;
import com.yadong.doge.rpc.invoker.Invoker;

import java.util.List;

/**
* @author YadongTan
* @date 2022/9/1 11:49
* @Description 加权轮询
*/
public class RoundRobinLoadBalance extends AbstractLoadBalance{
    @Override
    public HostInfo doSelect(List<HostInfo> hostInfos, Invoker invoker) {
        return null;
    }
}
