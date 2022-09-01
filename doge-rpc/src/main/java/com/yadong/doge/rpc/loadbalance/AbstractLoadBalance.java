package com.yadong.doge.rpc.loadbalance;

import com.yadong.doge.registry.config.HostInfo;
import com.yadong.doge.rpc.invoker.Invoker;

import java.util.List;

public abstract class AbstractLoadBalance implements LoadBalance{


    public int getWeight(HostInfo hostInfo) {
        return hostInfo.getHostData().getWeight();
    }

}
