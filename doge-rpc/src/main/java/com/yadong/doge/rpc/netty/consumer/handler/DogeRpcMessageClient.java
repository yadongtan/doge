package com.yadong.doge.rpc.netty.consumer.handler;

import com.yadong.doge.rpc.invoker.InvokedResult;
import com.yadong.doge.rpc.invoker.Invoker;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

public interface DogeRpcMessageClient{

    public Future<Object> syncSendInvoker(Invoker invoker);

}
