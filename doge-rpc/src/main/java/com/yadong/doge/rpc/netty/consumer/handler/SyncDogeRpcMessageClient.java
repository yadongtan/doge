package com.yadong.doge.rpc.netty.consumer.handler;

import com.yadong.doge.rpc.invoker.Invoker;
import com.yadong.doge.rpc.invoker.InvokerAndResultMap;
import com.yadong.doge.rpc.status.RpcStatus;
import com.yadong.doge.rpc.status.RpcStatusWatcher;
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
                RpcStatusWatcher watcher = new RpcStatusWatcher(invoker);   // 执行的记录
                watcher.start();
                Object result = null;
                try {
                    result = send(invoker);
                    watcher.stop();
                } catch (Exception e) {
                    e.printStackTrace();
                    watcher.stopFailed();
                    throw e;
                }
                return result;
            }
        });
        return objectFuture;
    }




    // must be implement with subclass!
    Object send(Invoker invoker) throws Exception;

}
