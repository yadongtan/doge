package com.yadong.doge.rpc.cluster;

import com.yadong.doge.registry.config.HostInfo;
import com.yadong.doge.rpc.directory.DynamicDirectory;
import com.yadong.doge.rpc.invoker.InvokedResult;
import com.yadong.doge.rpc.invoker.Invoker;
import com.yadong.doge.rpc.invoker.NettyClientHostMap;
import com.yadong.doge.rpc.loadbalance.LoadBalance;
import com.yadong.doge.rpc.loadbalance.LoadBalanceFactory;
import com.yadong.doge.rpc.netty.consumer.handler.SyncDogeRpcMessageClient;

import java.util.List;
import java.util.concurrent.Future;

/**
 * @author YadongTan
 * @date 2022/8/31 21:44
 * @Description 失败自动切换, 这是dubbo的默认容错方案, doge框架也将选择这个作为默认容错方案
 *              当调用失败时自动切换到其他可用的节点, 具体的重试次数和间隔时间可通过引用服务的时候配置,
 *              默认重试次数为1, 也就是只调用一次
 */
public class FailoverCluster  extends AbstractCluster{


    @Override
    public Object clusterInvoke(Invoker invoker) {
        try{
            // 获取主机
            return commonInvoke(invoker);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

}
