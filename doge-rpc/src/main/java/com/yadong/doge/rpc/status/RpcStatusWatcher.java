package com.yadong.doge.rpc.status;


import com.yadong.doge.rpc.invoker.Invoker;
import org.checkerframework.checker.units.qual.A;

import javax.sound.midi.SysexMessage;
import java.util.concurrent.atomic.AtomicLong;

/**
* @author YadongTan
* @date 2022/9/2 12:36
* @Description 提供RpcStatus的便捷及记录
*/
public class RpcStatusWatcher {

    private final Invoker invoker;
    private final RpcStatus status;
    private long startTime;
    private long endTime;
    private long elapsed;

    public RpcStatusWatcher(Invoker invoker){
        this.status = RpcStatus.getStatus(invoker.getHostInfo().getHostAndPort(), invoker.getMethod().getName());
        this.invoker = invoker;
    }

    // 开始远程调用时调用
    public void start() {
        startTime = System.currentTimeMillis();
        status.activeIncr();    //活跃计数器+1
        status.totalIncr();     //总次数+1
    }

    // 成功返回时调用
    public void stop(){
        endTime = System.currentTimeMillis();
        elapsed = endTime - startTime;
        status.activeDecr();    //活跃计数器-1
        status.totalElapsedUpdate(elapsed); //总响应时间 + 本次
        status.succeededMaxElaspsedUpdate(elapsed); //最大成功响应时间 比较
        status.maxElaspsedUpdate(elapsed);  //最大响应时间 比较
    }

    // 失败时调用
    public void stopFailed(){
        endTime = System.currentTimeMillis();
        elapsed = endTime - startTime;
        status.activeDecr();    //活跃计数器-1
        status.failedIncr();    //失败次数+1
        status.failedElaspsedUpdate(elapsed);   //失败的总响应时间 + 本次
        status.failedMaxElapsedUpdate(elapsed); //失败的最大响应时间  比较
        status.maxElaspsedUpdate(elapsed);  //最大响应时间 比较
    }

    public long getElapsed(){
        return elapsed;
    }
}
