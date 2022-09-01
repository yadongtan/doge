package com.yadong.doge.rpc.exception;

public class UnknownLoadBalanceException extends Exception{
    public UnknownLoadBalanceException(String msg){
        super(msg);
    }

}
