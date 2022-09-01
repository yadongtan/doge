package com.yadong.doge.rpc.loadbalance;


import com.yadong.doge.registry.config.HostInfo;
import com.yadong.doge.rpc.invoker.Invoker;

import java.util.List;

/**
 * @author YadongTan
 * @date 2022/8/31 19:33
 * @Description 负载均衡策略
 */
public interface LoadBalance {

    HostInfo doSelect(List<HostInfo> hostInfos, Invoker invoker);

}
