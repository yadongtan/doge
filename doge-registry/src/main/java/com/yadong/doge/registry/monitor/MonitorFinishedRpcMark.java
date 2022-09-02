package com.yadong.doge.registry.monitor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author YadongTan
 * @date 2022/9/2 15:20
 * @Description 当完成了一次Rpc调用后, 客户端发送此消息到监控中心
 */
public class MonitorFinishedRpcMark {

    private static final Logger logger = LoggerFactory.getLogger(MonitorFinishedRpcMark.class);

    private String hostAndPort;
    private String methodName;
    private String version;
    private boolean success;
    private long elapsed;   //调用耗费的时间

    public MonitorFinishedRpcMark(){

    }

    public MonitorFinishedRpcMark(String hostAndPort, String methodName, String version, boolean success, long elapsed){
        this.hostAndPort = hostAndPort;
        this.methodName = methodName;
        this.version = version;
        this.success = success;
        this.elapsed = elapsed;
    }

    public static Logger getLogger() {
        return logger;
    }

    public String getHostAndPort() {
        return hostAndPort;
    }

    public void setHostAndPort(String hostAndPort) {
        this.hostAndPort = hostAndPort;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public long getElapsed() {
        return elapsed;
    }

    public void setElapsed(long elapsed) {
        this.elapsed = elapsed;
    }
}
