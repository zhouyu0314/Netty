package com.zy.InOutBoundHandler;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

public class ClientInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        //加入一个出栈的handler，对数据进行编码
        pipeline.addLast(new MyLongToByteEncoder());
        //加入一个入栈的handler，对数据进行解码
        pipeline.addLast(new MyByteToLongDecoder());
        //加入一个自定义的handler，处理业务逻辑
        pipeline.addLast(new ClientHandler());
    }
}
