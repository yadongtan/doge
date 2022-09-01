package com.yadong.doge.rpc.netty.consumer.handler;

import com.yadong.doge.rpc.invoker.Invoker;
import com.yadong.doge.rpc.invoker.InvokerAndResultMap;
import com.yadong.doge.utils.ObjectMapperUtils;
import io.netty.channel.ChannelFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;

public interface SyncDogeRpcMessageClient extends DogeRpcMessageClient {

    ExecutorService executor = Executors.newCachedThreadPool();
    static final Logger logger = LoggerFactory.getLogger(SyncDogeRpcMessageClient.class);

    @Override
    default Future<Object> syncSendInvoker(Invoker invoker) {
        Future<Object> objectFuture = executor.submit(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                logger.info("执行call方法");
                return send(invoker);
            }
        });
        return objectFuture;
    }

    // must be implement with subclass!
    Object send(Invoker invoker) throws Exception;

}
