package com.yadong.doge.registry.config;

import com.yadong.doge.utils.NameGenerateUtils;

import java.util.Objects;

/**
* @author YadongTan
* @date 2022/8/30 10:52
* @Description 保存服务提供者的主机信息
*/
public class HostInfo {

    private String host;
    private int port;
    HostData hostData;  //一些主机提供者的信息,比如接口版本,运行状态, 权重

    public HostInfo(){
    }

    public HostInfo(String host, int port){
        this.host = host;
        this.port = port;
        hostData = new HostData();
    }

    public HostInfo(String hostAndPort, HostData hostData) {
        String[] strs = hostAndPort.split(":");
        this.host = strs[0];
        this.port = Integer.parseInt(strs[1]);
        this.hostData = hostData;
    }


    public String getHostAndPort(){
        return host + ":" + port;
    }

    public String getHost() {
        return host;
    }

    public HostInfo setHost(String host) {
        this.host = host;
        return this;
    }

    public int getPort() {
        return port;
    }

    public HostInfo setPort(int port) {
        this.port = port;
        return this;
    }

    public HostData getHostData() {
        return hostData;
    }

    public void setHostData(HostData hostData) {
        this.hostData = hostData;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("[").append(host).append(":").append(port).append("]###")
                .append(hostData.toString());
        return builder.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HostInfo hostInfo = (HostInfo) o;
        return port == hostInfo.port && Objects.equals(host, hostInfo.host);
    }

    @Override
    public int hashCode() {
        return Objects.hash(host, port);
    }
}
