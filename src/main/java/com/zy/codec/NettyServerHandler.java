package com.zy.codec;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

/**
 * 1.自定义一个Handler需要继承netty规定好的某个HandlerAdapter(规范)
 * 2.这时我们自定义一个的Handler，才能称之为一个Handler
 */
public class NettyServerHandler extends ChannelInboundHandlerAdapter {
    //读取数据的事件（这里我们可以读取客户端发送的消息）
    /*
    1.ChannelHandlerContext ctx 上下文对象，含有： 管道pipeline（业务逻辑的处理），通道（数据的传输），地址
    2.Object msg 客户端发送发送的数据
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //读取从客户端发送的SutdentPOJO.Student
        StudentPOJO.Student  student = (StudentPOJO.Student) msg;

        System.out.println("客户端发送的数据 id=" + student.getId());
        System.out.println("客户端发送的数据 name=" + student.getName());

    }

    //数据读取完毕
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        //将数据写入缓冲区并刷新
        //对发送的数据进行编码
        ctx.writeAndFlush(Unpooled.copiedBuffer("Hello，客户端～", CharsetUtil.UTF_8));

    }

    //处理异常，一般是需要关闭通道
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();

    }
}
