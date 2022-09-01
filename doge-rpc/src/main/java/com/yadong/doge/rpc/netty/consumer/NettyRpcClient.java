package com.yadong.doge.rpc.netty.consumer;


import com.yadong.doge.registry.config.HostInfo;
import com.yadong.doge.rpc.netty.consumer.handler.DogeClientMessageHandler;
import com.yadong.doge.rpc.netty.consumer.handler.SyncDogeRpcMessageClient;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
* @author YadongTan
* @date 2022/8/31 23:24
* @Description 启动与一个服务提供者的连接, 为TCP长连接, 通过返回的client进行通信
*/
public class NettyRpcClient {

    private static final Logger logger = LoggerFactory.getLogger(NettyRpcClient.class);

    public static SyncDogeRpcMessageClient createClient(HostInfo hostInfo) throws InterruptedException {
        NettyRpcClient nettyRpcClient = new NettyRpcClient();
        return nettyRpcClient.start(hostInfo);
    }

    public SyncDogeRpcMessageClient start(HostInfo hostInfo) throws InterruptedException {
        return start0(hostInfo.getHost(), hostInfo.getPort());
    }

    private SyncDogeRpcMessageClient start0(String hostname, int port) throws InterruptedException {
        DogeClientMessageHandler client = new DogeClientMessageHandler();
        NioEventLoopGroup group = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(group)
                .option(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        ChannelPipeline pipeline = socketChannel.pipeline();
                        pipeline//.addLast("framer", new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter()))
                                .addLast(new StringDecoder())
                                .addLast(new StringEncoder())
                                .addLast(client);
                    }
                });
        ChannelFuture channelFuture = bootstrap.connect(hostname, port).sync();
        Channel channel = channelFuture.channel();
        logger.info("Dong 启动 Client 连接[" + hostname + ":" + port + "] 成功");
        return client;
    }


}
