package com.zy.webSocket;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

public class NettyServer {
    public static void main(String[] args) throws Exception{
        NioEventLoopGroup bossGroup = new NioEventLoopGroup(1);
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            NettyServerFrameHandler nettyServerFrameHandler = new NettyServerFrameHandler();
            ServerBootstrap serverBootstrap = new ServerBootstrap().group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            //基于http协议，使用http编码和解码器
                            pipeline.addLast(new HttpServerCodec());
                            //是以块方式写，添加chunkedWriteHandler处理器
                            pipeline.addLast(new ChunkedWriteHandler());

                            /*
                            说明：
                            1.因为http数据在传输过程中是分段的，HttpObjectAggregator 就是可以将多个段聚合在一起
                            2.这就是为什么，当浏览器发送大量数据时，就会发出多次http请求。
                             */

                            pipeline.addLast(new HttpObjectAggregator(8192));
                            /*
                            说明：
                            1.对于webSocket，它的数据是以 帧（frame）形式传输
                            2.可以看到webSocketFrame 下面有六个子类
                            3.浏览器请求时 ws://localhost:7000/hello 表示请求的uri
                            4.WebSocketServerProtocolHandler 核心功能 将http协议升级为ws协议，保持长链接
                             */
                            pipeline.addLast(new WebSocketServerProtocolHandler("/hello"));

                            //自定义的handler，处理业务逻辑
                            pipeline.addLast(new NettyServerFrameHandler());
                        }
                    });

            ChannelFuture cf = serverBootstrap.bind(9000).sync();
            cf.channel().closeFuture().sync();






        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }

    }
}
