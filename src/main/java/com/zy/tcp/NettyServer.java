package com.zy.tcp;


import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class NettyServer {
    public static void main(String[] args)throws Exception {

        //1)创建BossGroup 和WorkerGroup
        /*
        1.创建两个线程组bossGroup和workerGroup
        2.bossGroup只处理连接请求，真正的和客户端业务处理，会交给workerGroup完成
        3.两个都是无限循环
        4.bossGroup和workerGroup含有的(NioEventLoop)的个数，默认是cpu核心数*2(NettyRuntime.availableProcessors() )
         */
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {


            //2)创建服务器端的启动对象，配置启动的参数
            ServerBootstrap bootstrap = new ServerBootstrap();

            //使用链式编程的方式进行设置
            bootstrap.group(bossGroup, workerGroup)//设置两个线程组
                    .channel(NioServerSocketChannel.class)//使用NioSocketChannel作为服务器的通道实现
                    .option(ChannelOption.SO_BACKLOG, 128)//设置线程队列得到连接个数
                    .childOption(ChannelOption.SO_KEEPALIVE, true)//设置保持活动连接状态
                    .handler(null)//该handle对应的是bossGroup，如果不想在accept设置业务处理逻辑就不用写
                    .childHandler(new ChannelInitializer<SocketChannel>() {//创建一个通道初始化对象（匿名对象）该handler对应的是workerGroup
                //给pipeline设置处理器
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    //每一个客户进来都会有一个独立的Channel,可以将此cide加入一个map用于推送具体用户
                    System.out.println("当前clinet的ScoketChannel的hashcode：" + ch.hashCode());
                    ch.pipeline().addLast(new NettyServerHandler());
                }
            });//给workerGroup的EventLoop对应的管道设置处理器

            System.out.println("*******服务器 is ready*******");

            //绑定一个端口并且同步生成一个ChannelFuture对象
            //启动服务器并绑定端口
            ChannelFuture cf = bootstrap.bind(6668).sync();

            //给cf注册监听器，来监听关心的事件
            /*
            通过 isDone 方法来判断当前操作是否完成；
            通过 isSuccess 方法来判断已完成的当前操作是否成功；
            通过 getCause 方法来获取已完成的当前操作失败的原因；
            通过 isCancelled 方法来判断已完成的当前操作是否被取消；
            通过 addListener 方法来注册监听器，当操作已完成(isDone 方法返回完成)，将会通知指定的监听器；如果 Future 对象已完成，则通知指定的监听器
             */
            cf.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture channelFuture) throws Exception {
                    if (cf.isSuccess()) {
                        System.out.println("监听端口6668成功！");
                    } else {
                        System.out.println("监听端口6668失败！");
                    }
                }
            });

            //对关闭通道进行监听
            cf.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }

    }
}
