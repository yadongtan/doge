package com.yadong.doge.rpc.exception;

public class UnknownClusterException extends RuntimeException{

    public UnknownClusterException(String msg){
        super(msg);
    }

}