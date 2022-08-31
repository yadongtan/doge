package com.yadong.doge.rpc.netty.consumer.handler;

import com.yadong.doge.rpc.invoker.InvokedResult;
import com.yadong.doge.rpc.invoker.Invoker;

import java.util.concurrent.Callable;

public interface DogeRpcMessageClient{

    public void syncSendInvoker(Invoker invoker);

}
