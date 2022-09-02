package com.yadong.doge.rpc.netty.monitor;

import com.yadong.doge.registry.config.HostInfo;
import com.yadong.doge.rpc.netty.consumer.NettyRpcClient;
import com.yadong.doge.rpc.netty.consumer.handler.DogeClientMessageHandler;
import com.yadong.doge.rpc.netty.consumer.handler.SyncDogeRpcMessageClient;
import com.yadong.doge.rpc.netty.monitor.handler.MonitorSendMarkHandler;
import com.yadong.doge.rpc.netty.monitor.handler.SyncMonitorClient;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
* @author YadongTan
* @date 2022/9/2 15:57
* @Description 连接监控中心的Netty客户端
*/
public class MonitorNettyClient {

    private static final Logger logger = LoggerFactory.getLogger(NettyRpcClient.class);

    public static SyncMonitorClient createClient(HostInfo hostInfo) throws InterruptedException {
        MonitorNettyClient monitorClient = new MonitorNettyClient();
        return monitorClient.start(hostInfo);
    }

    private SyncMonitorClient start(HostInfo hostInfo) throws InterruptedException {
        return start0(hostInfo.getHost(), hostInfo.getPort());
    }

    private SyncMonitorClient start0(String hostname, int port) throws InterruptedException {
        MonitorSendMarkHandler client = new MonitorSendMarkHandler();
        NioEventLoopGroup group = new NioEventLoopGroup(8);
        Bootstrap bootstrap = new Bootstrap();
        ByteBuf delimiter = Unpooled.copiedBuffer("\r\n".getBytes());
        bootstrap.group(group)
                .option(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        ChannelPipeline pipeline = socketChannel.pipeline();
                        pipeline.addFirst(new DelimiterBasedFrameDecoder(60000,delimiter))
                                //.addLast(new LineBasedFrameDecoder(1024))
                                .addLast(new StringDecoder())
                                .addLast(new StringEncoder())
                                .addLast(client);
                    }
                });
        ChannelFuture channelFuture = bootstrap.connect(hostname, port).sync();
        Channel channel = channelFuture.channel();
        logger.info("Doge 启动 Client 连接 监控中心[" + hostname + ":" + port + "] 成功");
        return client;
    }


}
