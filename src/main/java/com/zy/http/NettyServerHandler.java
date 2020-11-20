package com.zy.http;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

/**
 * SimpleChannelInboundHandler 是 ChannelInboundHandlerAdapter 的子类
 * HttpObject 客户端和服务器端相互通讯的数据被封装成的类型
 */

public class NettyServerHandler extends SimpleChannelInboundHandler<HttpObject> {
    //当有读取事件就绪的时候会调用此
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg) throws Exception {
        //判断msg是不是HttpRequest请求
        if (msg instanceof HttpRequest) {
            HttpRequest request = (HttpRequest) msg;
            if ("/favicon.ico".equals(request.uri())) {
                System.out.println(request.uri() + "\t" + "不予处理");
                return;
            }

            System.out.println("msg类型：" + msg.getClass());
            System.out.println("客户端地址：" + ctx.channel().remoteAddress());

            //回复信息给浏览器【http协议】
            //String str = new String("hello,我是服务器".getBytes("ISO-8859-1"));
            ByteBuf byteBuf = Unpooled.copiedBuffer("hello,我是服务器", CharsetUtil.UTF_8);
            //构造一个http响应（httpResponse） 协议版本 状态码 内容
            FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, byteBuf);
            HttpHeaders headers = response.headers();
//            headers.set(HttpHeaderNames.CONTENT_TYPE,"text/plain");
            headers.set(HttpHeaderNames.CONTENT_TYPE, "application/json;charset=utf-8");
            headers.set(HttpHeaderNames.CONTENT_LENGTH, byteBuf.readableBytes());

            //将构建好的response返回

            ctx.writeAndFlush(response);

        }

    }
}
