package com.yadong.doge.rpc.exception;

/**
* @author YadongTan
* @date 2022/8/31 16:31
* @Description 为需要远程调用的接口代理失败时抛出此异常
*/
public class ServiceProxyException extends RuntimeException{

    public ServiceProxyException(String msg){
        super(msg);
    }

}
