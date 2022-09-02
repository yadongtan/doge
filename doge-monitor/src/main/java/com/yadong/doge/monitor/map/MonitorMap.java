package com.yadong.doge.monitor.map;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author YadongTan
 * @date 2022/9/1 13:25
 * @Description 客户端的计数器
 * 比如记录当前通过某个服务提供者的请求还未完全的总数
 */
public class MonitorMap {

    private static final Logger logger = LoggerFactory.getLogger(MonitorMap.class);

    private final AtomicLong total = new AtomicLong();  //总次数
    private final AtomicInteger failed = new AtomicInteger();   //失败次数
    private final AtomicLong totalElapsed = new AtomicLong();   //总响应时间
    private final AtomicLong failedElapsed = new AtomicLong(); //失败的总响应时间
    private final AtomicLong maxElapsed = new AtomicLong();    //最大响应时间
    private final AtomicLong failedMaxElapsed = new AtomicLong(); //失败的最大响应时间
    private final AtomicLong succeededMaxElapsed = new AtomicLong();    //最大成功响应时间


    // hostAndPort -- rpcStatusMap
    private static final ConcurrentHashMap<String, ConcurrentHashMap<String, MonitorMap>>
        hostAndMethodMap = new ConcurrentHashMap<>(16);

//    public static int getActiveStatus(String hostAndPort, String methodName){
//        ConcurrentHashMap<String, MonitorRpcStatus> rpcStatusMap = hostAndMethodMap.get(hostAndPort);
//        if(rpcStatusMap == null || rpcStatusMap.isEmpty()){
//            return 0;
//        }
//        MonitorRpcStatus monitorRpcStatus = rpcStatusMap.get(methodName);
//        if(monitorRpcStatus == null){
//            return 0;
//        }
//        return monitorRpcStatus.getActive();
//    }

    public static MonitorMap getMonitorInfo(String hostAndPort, String methodName){
        ConcurrentHashMap<String, MonitorMap> rpcStatusMap = hostAndMethodMap.computeIfAbsent(hostAndPort, k -> {
            return new ConcurrentHashMap<String, MonitorMap>();
        });
        MonitorMap monitorRpcStatus = rpcStatusMap.computeIfAbsent(methodName, k -> {
            return new MonitorMap();
        });
        return monitorRpcStatus;
    }


    public long getTotal() {
        return total.get();
    }

    public long getFailed() {
        return failed.get();
    }

    public long getTotalElapsed() {
        return totalElapsed.get();
    }

    public long getFailedElapsed() {
        return failedElapsed.get();
    }

    public long getMaxElapsed() {
        return maxElapsed.get();
    }

    public long getFailedMaxElapsed() {
        return failedMaxElapsed.get();
    }

    public long getSucceededMaxElapsed() {
        return succeededMaxElapsed.get();
    }


    public void totalIncr(){
        total.incrementAndGet();
    }

    public void failedIncr(){
        failed.incrementAndGet();
    }

    public void totalElapsedUpdate(long addTime){
        totalElapsed.addAndGet(addTime);
    }

    public void failedElaspsedUpdate(long addTime) {
        totalElapsed.addAndGet(addTime);
    }

    public void maxElaspsedUpdate(long newTime){
        long oldTime = maxElapsed.get();
        while (newTime > oldTime) {
            if (maxElapsed.compareAndSet(oldTime, newTime)) {
                break;
            }
            oldTime = maxElapsed.get(); //说明值被其他线程换了, 需要查看一下最新值
        }
    }

    public void failedMaxElapsedUpdate(long newTime){
        long oldTime = failedMaxElapsed.get();
        while (newTime > oldTime) {
            if (failedMaxElapsed.compareAndSet(oldTime, newTime)) {
                break;
            }
            oldTime = failedMaxElapsed.get(); //说明值被其他线程换了, 需要查看一下最新值
        }
    }

    public void succeededMaxElaspsedUpdate(long newTime) {
        long oldTime = succeededMaxElapsed.get();
        while (newTime > oldTime) {
            if (succeededMaxElapsed.compareAndSet(oldTime, newTime)) {
                break;
            }
            oldTime = succeededMaxElapsed.get(); //说明值被其他线程换了, 需要查看一下最新值
        }
    }

    public static String show() {
        StringBuilder builder = new StringBuilder();
        hostAndMethodMap.forEach((hostAndPort, methodAndStatusMap) -> {
            builder.append("<br/>主机[").append(hostAndPort).append("]:<br/>");
            methodAndStatusMap.forEach((methodName , status) ->{
                builder.append("<br/>methodName:[").append(methodName).append("]:<br/>");
                builder.append("<br/>总调用次数:").append(Long.toString(status.getTotal()));
                builder.append("<br/>失败次数:").append(Long.toString(status.getFailed()));
                builder.append("<br/>总响应时间:").append(Long.toString(status.getTotalElapsed()));
                builder.append("<br/>最大响应时间").append(Long.toString(status.getMaxElapsed()));
                builder.append("<br/>失败的总响应时间").append(Long.toString(status.getFailedElapsed()));
                builder.append("<br/>最大成功响应时间").append(Long.toString(status.getMaxElapsed()));
                builder.append("<br/>失败的最大响应时间").append(Long.toString(status.getFailedElapsed()));
                builder.append("<br/>======================================================================\n");
            });
        });
        return builder.toString();
    }
}
