package com.yadong.doge.monitor.netty.handler;

import com.yadong.doge.registry.monitor.MonitorFinishedRpcMark;
import com.yadong.doge.monitor.map.MonitorRpcFinishedRecorder;
import com.yadong.doge.utils.ObjectMapperUtils;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RemotingMonitorHandler extends ChannelInboundHandlerAdapter {

    private static final ExecutorService executor = Executors.newCachedThreadPool();
    private static final Logger logger = LoggerFactory.getLogger(RemotingMonitorHandler.class);

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
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        String json = (String) msg;
        MonitorFinishedRpcMark mark = ObjectMapperUtils.toObject(json, MonitorFinishedRpcMark.class);
        if(mark == null){
            return;
        }
        MonitorRpcFinishedRecorder.record(mark);
        logger.info("记录了一次调用, host:[" + mark.getHostAndPort() + "]");
    }

}
