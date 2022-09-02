package com.yadong.doge.rpc.netty.monitor.handler;

import com.yadong.doge.registry.monitor.MonitorFinishedRpcMark;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public interface SyncMonitorClient{


    ExecutorService executor = Executors.newSingleThreadExecutor();

    default void syncSend(MonitorFinishedRpcMark mark){
        executor.submit(()->{
            send(mark);
        });
    }

    //implement by subclasses...
    void send(MonitorFinishedRpcMark mark);

}
