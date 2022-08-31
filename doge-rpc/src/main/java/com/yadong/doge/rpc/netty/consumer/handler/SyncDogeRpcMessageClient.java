package com.yadong.doge.rpc.netty.consumer.handler;

import com.yadong.doge.rpc.invoker.Invoker;
import com.yadong.doge.rpc.invoker.InvokerAndResultMap;
import com.yadong.doge.utils.ObjectMapperUtils;
import io.netty.channel.ChannelFuture;

import java.util.concurrent.*;

public interface SyncDogeRpcMessageClient extends DogeRpcMessageClient {

    ExecutorService executor = Executors.newCachedThreadPool();

    @Override
    default void syncSendInvoker(Invoker invoker) {
        Future<Object> objectFuture = executor.submit(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                return send(invoker);
            }
        });
    }

    // must be implement with subclass!
    Object send(Invoker invoker) throws Exception;

}
