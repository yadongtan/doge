package com.yadong.doge.registry.config;

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
}
