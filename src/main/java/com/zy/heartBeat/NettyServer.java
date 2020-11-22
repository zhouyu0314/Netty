package com.zy.heartBeat;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

public class NettyServer {
    public static void main(String[] args)throws Exception {

        NioEventLoopGroup bossGroup = new NioEventLoopGroup(1);
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            //加入一个netty 提供 IdleStateHandler
                            /*
                            说明
                            1.IdleStateHandler netty 提供的处理/检测 处理器空闲状态的处理器
                                long readerIdleTime：表示多长时间没有读，就会发送一个心跳检测包检测是否连接
                                long writerIdleTime：表示多长时间没有写，就会发送一个心跳检测包检测是否连接
                                long allIdleTime：表示多长事件没有读写，就会发送一个心跳检测包检测是否连接

                            2.当IdleStateHandler触发后，就会传递给管道的下一个handler去处理，通过调用（触发）
                            下一个handler的userEventTriggered,在该方法中处理IdleStateHandler(读空闲、写空闲、读写空闲)
                             */
                            pipeline.addLast(new IdleStateHandler(3, 5, 7, TimeUnit.SECONDS));

                            //加入一个对空闲检测进一步处理的handler(自定义)
                            pipeline.addLast(new NettyServerHandler());

                        }
                    });
            ChannelFuture cf = serverBootstrap.bind(7000).sync();
            cf.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }


    }


}
