package com.yadong.doge.rpc.status;


import org.checkerframework.checker.units.qual.C;
import org.omg.PortableInterceptor.ACTIVE;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author YadongTan
 * @date 2022/9/1 13:25
 * @Description 客户端的计数器
 * 比如记录当前通过某个服务提供者的请求还未完全的总数
 */
public class RpcStatus {

    private final AtomicInteger active = new AtomicInteger();   //计数器
    private final AtomicLong total = new AtomicLong();  //总次数
    private final AtomicInteger failed = new AtomicInteger();   //失败次数
    private final AtomicLong totalElasped = new AtomicLong();   //总响应时间
    private final AtomicLong failedElaspsed = new AtomicLong(); //失败的总响应时间
    private final AtomicLong maxElaspsed = new AtomicLong();    //最大响应时间
    private final AtomicLong failedMaxElapsed = new AtomicLong(); //失败的总响应时间
    private final AtomicLong succeededMaxElaspsed = new AtomicLong();    //最大响应时间


    // hostAndPort -- rpcStatusMap
    private static final ConcurrentHashMap<String, ConcurrentHashMap<String, RpcStatus>>
        hostAndMethodMap = new ConcurrentHashMap<>(16);

    public static int getActiveStatus(String hostAndPort, String methodName){
        ConcurrentHashMap<String, RpcStatus> rpcStatusMap = hostAndMethodMap.get(hostAndPort);
        if(rpcStatusMap == null || rpcStatusMap.isEmpty()){
            return 0;
        }
        RpcStatus rpcStatus = rpcStatusMap.get(methodName);
        if(rpcStatus == null){
            return 0;
        }
        return rpcStatus.getActive();
    }

    public static RpcStatus getStatus(String hostAndPort, String methodName){
        ConcurrentHashMap<String, RpcStatus> rpcStatusMap = hostAndMethodMap.computeIfAbsent(hostAndPort, k -> {
            return new ConcurrentHashMap<String, RpcStatus>();
        });
        RpcStatus rpcStatus = rpcStatusMap.computeIfAbsent(methodName, k -> {
            return new RpcStatus();
        });
        return rpcStatus;

    }

    public int getActive() {
        return active.get();
    }

    public long getTotal() {
        return total.get();
    }

    public long getFailed() {
        return failed.get();
    }

    public long getTotalElasped() {
        return totalElasped.get();
    }

    public long getFailedElaspsed() {
        return failedElaspsed.get();
    }

    public long getMaxElaspsed() {
        return maxElaspsed.get();
    }

    public long getFailedMaxElapsed() {
        return failedMaxElapsed.get();
    }

    public long getSucceededMaxElaspsed() {
        return succeededMaxElaspsed.get();
    }



}
