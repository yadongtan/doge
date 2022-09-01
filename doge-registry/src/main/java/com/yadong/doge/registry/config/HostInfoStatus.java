package com.yadong.doge.registry.config;

/**
* @author YadongTan
* @date 2022/9/1 11:49
* @Description 主机的状态
*/
public enum HostInfoStatus {

    RUNNING(1),
    DOWN(-1);

    private int status;

    private HostInfoStatus(int status){
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
