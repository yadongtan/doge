package com.yadong.doge.monitor.netty;

import com.yadong.doge.config.MonitorProperties;
import com.yadong.doge.config.ProviderProperties;
import com.yadong.doge.monitor.netty.handler.RemotingMonitorHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class MonitorNettyServer {


    private static final Logger logger = LoggerFactory.getLogger(MonitorNettyServer.class);

    private MonitorNettyServer(){}

    private static MonitorNettyServer _INSTANCE;

    public static MonitorNettyServer getInstance() {
        if(_INSTANCE == null){
            synchronized (MonitorNettyServer.class){
                if(_INSTANCE == null){
                    _INSTANCE = new MonitorNettyServer();
                }
            }
        }
        return _INSTANCE;
    }

    // the really method to start provider server
    public void syncStart(MonitorProperties properties){
        new Thread(()->{
            start(properties);
        }).start();
    }


    // the really method to start provider server
    public void start(MonitorProperties properties){
        int port = properties.getMonitorPort();
        if(properties.getMonitorHost() != null){
            start0(properties.getMonitorHost(), port);
        }else{
            try {
                String hostAddress = InetAddress.getLocalHost().getHostAddress();
                start0(hostAddress, port);
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
        }

    }

    private void start0(String hostname, int port){
        NioEventLoopGroup bossGroup = new NioEventLoopGroup(1);
        NioEventLoopGroup workerGroup = new NioEventLoopGroup(64);
        ServerBootstrap bootstrap = new ServerBootstrap();
        ByteBuf delimiter = Unpooled.copiedBuffer("\r\n".getBytes());
        bootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childOption(ChannelOption.TCP_NODELAY, true)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        ChannelPipeline pipeline = socketChannel.pipeline();
                        pipeline.addFirst(new DelimiterBasedFrameDecoder(60000,delimiter))
                                //.addLast(new LineBasedFrameDecoder(1024))
                                .addLast(new StringDecoder())
                                .addLast(new StringEncoder())
                                .addLast(new RemotingMonitorHandler());
                    }
                });
        try {
            ChannelFuture channelFuture = bootstrap.bind(hostname, port);
            Channel channel = channelFuture.channel();
            logger.info("Doge 启动 Monitor [" + hostname + ":" + port + "] 成功");
            //channelFuture.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
