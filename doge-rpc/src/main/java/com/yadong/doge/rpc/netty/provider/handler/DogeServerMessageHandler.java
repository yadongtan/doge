package com.yadong.doge.rpc.netty.provider.handler;

import com.yadong.doge.rpc.invoker.InvokedResult;
import com.yadong.doge.rpc.invoker.Invoker;
import com.yadong.doge.utils.ObjectMapperUtils;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DogeServerMessageHandler extends ChannelInboundHandlerAdapter{

    private static final Logger logger = LoggerFactory.getLogger(DogeServerMessageHandler.class);

    private ChannelHandlerContext context;
    private String result;  //调用方法返回的结果
    private String param;   //客户端调用方法时, 传入的参数, 遵循自己的协议

    //消费者与本服务提供者建立连接后, 此方法设置好上下文
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        context = ctx;
        logger.info("服务器接收到连接请求...");
    }

    //读取到来自消费者的方法调用请求
    @Override
    public synchronized void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//        ByteBuf buf= (ByteBuf) msg;
//        String message = buf.toString(CharsetUtil.UTF_8);
        String message = (String) msg;
        logger.info("接收到远程调用请求:[" + message + " ]");
        Invoker invoker = ObjectMapperUtils.toObject(message, Invoker.class);
        InvokedResult invokedResult = invoker.invoke();
        logger.info("生成执行结果:[" + invokedResult + "]");
        ctx.writeAndFlush(ObjectMapperUtils.toJSON(invokedResult));
    }

}
