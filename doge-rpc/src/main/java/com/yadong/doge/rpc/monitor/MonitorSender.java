package com.yadong.doge.rpc.monitor;


import com.yadong.doge.config.ConsumerProperties;
import com.yadong.doge.registry.config.HostInfo;
import com.yadong.doge.registry.monitor.MonitorFinishedRpcMark;
import com.yadong.doge.rpc.netty.monitor.MonitorNettyClient;
import com.yadong.doge.rpc.netty.monitor.handler.MonitorSendMarkHandler;
import com.yadong.doge.rpc.netty.monitor.handler.SyncMonitorClient;


public class MonitorSender {

    private SyncMonitorClient handler;

    private static MonitorSender _INSTANCE = null;

    private MonitorSender(String host,int port){
        try {
            handler = MonitorNettyClient.createClient(new HostInfo(host, port));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void setClient(MonitorSendMarkHandler handler){
        this.handler = handler;
    }

    public static MonitorSender getInstance(){
        return _INSTANCE;
    }

    public static MonitorSender createInstance(ConsumerProperties consumerProperties){
        _INSTANCE = new MonitorSender(consumerProperties.getMonitorHost(), consumerProperties.getMonitorPort());
        return _INSTANCE;
    }

    public void syncSend(MonitorFinishedRpcMark mark){
        handler.syncSend(mark);
    }

}
