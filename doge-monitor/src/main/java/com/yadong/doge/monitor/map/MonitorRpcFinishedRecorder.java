package com.yadong.doge.monitor.map;


import com.yadong.doge.registry.monitor.MonitorFinishedRpcMark;

/**
* @author YadongTan
* @date 2022/9/2 12:36
* @Description 将接收到的记录转换为监控中心的记录
*/
public class MonitorRpcFinishedRecorder {

    public static void record(MonitorFinishedRpcMark mark){
        MonitorMap monitorInfo = MonitorMap.getMonitorInfo(mark.getHostAndPort(), mark.getMethodName());
        long elapsed = mark.getElapsed();
        if(mark.isSuccess()){
            finished(monitorInfo, elapsed);
        }else{
            finishedWithException(monitorInfo, elapsed);
        }
    }

    // 成功返回时调用
    private static void finished(MonitorMap monitorInfo, long elapsed){
        monitorInfo.totalIncr();     //总次数+1
        monitorInfo.totalElapsedUpdate(elapsed); //总响应时间 + 本次
        monitorInfo.succeededMaxElaspsedUpdate(elapsed); //最大成功响应时间 比较
        monitorInfo.maxElaspsedUpdate(elapsed);  //最大响应时间 比较
    }

    // 失败时调用
    private static void finishedWithException(MonitorMap monitorInfo, long elapsed){
        monitorInfo.totalIncr();     //总次数+1
        monitorInfo.failedIncr();    //失败次数+1
        monitorInfo.failedElaspsedUpdate(elapsed);   //失败的总响应时间 + 本次
        monitorInfo.failedMaxElapsedUpdate(elapsed); //失败的最大响应时间  比较
        monitorInfo.maxElaspsedUpdate(elapsed);  //最大响应时间 比较
    }

}
