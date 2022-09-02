package com.yadong.doge.registry.config;

import org.apache.catalina.Host;

public class HostData {
    private int weight; //权重    -1表示没设置权重
    private HostInfoStatus status;    //运行状态
    private String version; //服务提供者版本

    public HostData(){
        status = HostInfoStatus.RUNNING;
        weight = 100;
        version = "default";
    }

    public int getWeight() {
        return weight;
    }

    public HostData setWeight(int weight) {
        this.weight = weight;
        return this;
    }

    public HostInfoStatus getStatus() {
        return status;
    }

    public HostData setStatus(HostInfoStatus status) {
        this.status = status;
        return this;
    }

    public String getVersion() {
        return version;
    }

    public HostData setVersion(String version) {
        this.version = version;
        return this;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("weight=").append(weight).append("?")
                .append("status=").append(status).append("?")
                .append("version=").append(version);
        return builder.toString();
    }
}
