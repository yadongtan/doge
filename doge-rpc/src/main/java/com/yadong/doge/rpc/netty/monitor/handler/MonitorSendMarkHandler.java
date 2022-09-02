package com.yadong.doge.rpc.netty.monitor.handler;

import com.yadong.doge.registry.monitor.MonitorFinishedRpcMark;
import com.yadong.doge.rpc.netty.consumer.handler.DogeClientMessageHandler;
import com.yadong.doge.utils.ObjectMapperUtils;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Callable;

public class MonitorSendMarkHandler extends ChannelInboundHandlerAdapter implements SyncMonitorClient{

    private static final Logger logger = LoggerFactory.getLogger(DogeClientMessageHandler.class);

    private ChannelHandlerContext context;

    //与服务器建立连接之后, 此方法被调用, 设置好上下文
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        context = ctx;
        logger.info("客户端与监控中心建立连接");
    }


    @Override
    public void send(MonitorFinishedRpcMark mark) {
        context.writeAndFlush(ObjectMapperUtils.toJSON(mark) + "\r\n");
        logger.info("调用完成,异步发送记录到监控中心");
    }


}
