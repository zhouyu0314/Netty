package com.zy.groupChat;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.util.Scanner;

public class NettyClient {
    private final String host;
    private final int port;

    public NettyClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void run()throws Exception {
        NioEventLoopGroup eventExecutors = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap().group(eventExecutors)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast("decoder",new StringDecoder());
                            ch.pipeline().addLast("encoder",new StringEncoder());
                            ch.pipeline().addLast(new NettyClientHandler());
                        }
                    });

            ChannelFuture cf = bootstrap.connect(host, port).sync();
            //得到cannel
            Channel channel = cf.channel();
            System.out.println("----------" + "\t" + channel.localAddress() + "\t" + "----------");
            //客户端输入信息
            Scanner scanner = new Scanner(System.in);
            while (scanner.hasNext()) {
                String str = scanner.nextLine();
                //发送至服务端
                channel.writeAndFlush(str + "\r\n");
            }
            cf.channel().closeFuture().sync();
        } finally {
            eventExecutors.shutdownGracefully();
        }
    }

    public static void main(String[] args)throws Exception {
        new NettyClient("127.0.0.1",7000).run();
    }
}
