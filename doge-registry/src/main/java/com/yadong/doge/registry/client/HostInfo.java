package com.yadong.doge.registry.client;

/**
* @author YadongTan
* @date 2022/8/30 10:52
* @Description 保存服务提供者的主机信息
*/
public class HostInfo {

    private String host;
    private int port;

    public HostInfo(){
    }

    public HostInfo(String host, int port){
        this.host = host;
        this.port = port;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    @Override
    public String toString() {
        return host + ":" +port;
    }
}
