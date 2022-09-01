package com.yadong.doge.rpc.netty.consumer.handler;

import com.yadong.doge.rpc.invoker.InvokedResult;
import com.yadong.doge.rpc.invoker.Invoker;
import com.yadong.doge.rpc.invoker.InvokerAndResultMap;
import com.yadong.doge.utils.ObjectMapperUtils;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import jdk.internal.org.objectweb.asm.tree.IincInsnNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;


public class DogeClientMessageHandler extends ChannelInboundHandlerAdapter implements SyncDogeRpcMessageClient {

    private static final ConcurrentHashMap<Integer, InvokerAndResultMap> lockMap= new ConcurrentHashMap<>();

    private static final Logger logger = LoggerFactory.getLogger(DogeClientMessageHandler.class);

    private static final ThreadLocal<Object> threadLocal = new ThreadLocal<>();

    private ChannelHandlerContext context;

    //与服务器建立连接之后, 此方法被调用, 设置好上下文
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        context = ctx;
        logger.info("客户端与服务器建立连接");
    }

    //第一次请求调用的时候, 发起请求的线程进入阻塞, 等待唤醒
    //此方法在接收到调用结果后, 唤醒等待的线程
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        logger.info("接收到远程调用结果:[" + msg + "]");
        InvokedResult invokedResult = ObjectMapperUtils.toObject((String) msg, InvokedResult.class);
        InvokerAndResultMap invokerAndResultMap = lockMap.get(invokedResult.getLockId());
        invokerAndResultMap.setInvokedResult(invokedResult);
//        synchronized (lockMap.get(invokedResult.getLockId())){
//            notify();
//        }
        synchronized (invokerAndResultMap){
            invokerAndResultMap.notify(); //等待channelRead 方法获取到获取的结果后, 会唤醒这个线程
        }
    }

    public Object send(Invoker invoker) throws Exception {
        InvokerAndResultMap invokerAndResultMap = new InvokerAndResultMap(invoker);
        String json = ObjectMapperUtils.toJSON(invoker) + "\n";
        // only lock current invoker and its thread !
        lockMap.put(invoker.getLockId(), invokerAndResultMap);
            //发起远程调用
        ChannelFuture channelFuture = context.writeAndFlush(json + "\r\n");
        ChannelFuture sync = channelFuture.sync();
            //进行wait
        synchronized (invokerAndResultMap){
            logger.info("进入同步代码块");
            invokerAndResultMap.wait(); //等待channelRead 方法获取到获取的结果后, 会唤醒这个线程
        }
        InvokedResult invokedResult = invokerAndResultMap.getInvokedResult();
        String objJson = invokedResult.getObj();
        Object result = ObjectMapperUtils.toObject(objJson, Class.forName(invokedResult.getClassName()));
        logger.info("result:[" + result + "]");
        return result;
    }



}
