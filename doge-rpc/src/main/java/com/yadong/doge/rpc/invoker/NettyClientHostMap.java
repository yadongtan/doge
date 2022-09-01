package com.yadong.doge.rpc.invoker;

import com.yadong.doge.registry.config.HostInfo;
import com.yadong.doge.rpc.netty.consumer.NettyRpcClient;
import com.yadong.doge.rpc.netty.consumer.handler.SyncDogeRpcMessageClient;
import org.checkerframework.checker.units.qual.C;

import java.util.concurrent.ConcurrentHashMap;

/**
* @author YadongTan
* @date 2022/9/1 15:45
* @Description 维持与一个服务提供者的NETTY连接客户端
* key - client
*/
public class NettyClientHostMap {

   private static final ConcurrentHashMap<HostInfo, SyncDogeRpcMessageClient> clientMap = new ConcurrentHashMap<>(64);

    // 如果没有,则创建一个连接
    public static SyncDogeRpcMessageClient get(HostInfo hostInfo) throws InterruptedException {
        SyncDogeRpcMessageClient client = clientMap.get(hostInfo);
        if (client == null) {
            synchronized (hostInfo) {
                if (clientMap.get(hostInfo) == null) {
                    client = NettyRpcClient.createClient(hostInfo);
                    clientMap.put(hostInfo, client);
                }
            }
            return clientMap.get(hostInfo);
        }else{
            return client;
        }
    }

}
