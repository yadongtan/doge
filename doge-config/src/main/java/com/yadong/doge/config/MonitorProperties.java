package com.yadong.doge.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
* @author YadongTan
* @date 2022/9/2 16:02
* @Description 监控中心需要的配置信息
*/
@ConfigurationProperties(prefix = "doge.monitor")
public class MonitorProperties {
    private String monitorHost;
    private int monitorPort;
    private boolean enable;

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public String getMonitorHost() {
        return monitorHost;
    }

    public void setMonitorHost(String monitorHost) {
        this.monitorHost = monitorHost;
    }

    public int getMonitorPort() {
        return monitorPort;
    }

    public void setMonitorPort(int monitorPort) {
        this.monitorPort = monitorPort;
    }
}
