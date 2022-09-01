package com.yadong.doge.rpc.cluster;

import com.yadong.doge.registry.config.HostInfo;
import com.yadong.doge.rpc.directory.DynamicDirectory;
import com.yadong.doge.rpc.exception.UnknownLoadBalanceException;
import com.yadong.doge.rpc.invoker.Invoker;
import com.yadong.doge.rpc.invoker.NettyClientHostMap;
import com.yadong.doge.rpc.loadbalance.LoadBalance;
import com.yadong.doge.rpc.loadbalance.LoadBalanceFactory;
import com.yadong.doge.rpc.netty.consumer.handler.SyncDogeRpcMessageClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;


public abstract class AbstractCluster implements Cluster {

    DynamicDirectory directory = DynamicDirectory.getInstance();

    public Object commonInvoke(Invoker invoker) throws UnknownLoadBalanceException, ExecutionException, InterruptedException {
        // 获取主机
        List<HostInfo> hostInfos = directory.search(invoker);
        // 负债均线
        LoadBalance loadBalance = LoadBalanceFactory.getLoadBalance(invoker);
        HostInfo hostInfo = loadBalance.doSelect(hostInfos, invoker);
        // 获取客户端
        SyncDogeRpcMessageClient client = NettyClientHostMap.get(hostInfo);
        // 异步调用
        Future<Object> objectFuture = client.syncSendInvoker(invoker);
        // 阻塞等待返回
        return objectFuture.get();
    }

}
