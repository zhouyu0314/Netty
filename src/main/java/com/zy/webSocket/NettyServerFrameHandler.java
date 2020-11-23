package com.zy.webSocket;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * TextWebSocketFrame 类型，表示一个文本帧（Frame）
 * 范型的内容是客户端与服务端交互的内容的类型
 */
public class NettyServerFrameHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        System.out.println("服务器端收到消息：" + msg.text());

        //回复浏览器
        ctx.channel().writeAndFlush(new TextWebSocketFrame("服务器时间:" + sdf.format(new Date())) + "\t" + msg.text());


    }

    /**
     * 当web客户端连接后，就会触发
     * @param ctx
     * @throws Exception
     */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        //id表示唯一的值，LongText 是唯一的 ShortText不是唯一的
        System.out.println("handlerAdded 被调用" + ctx.channel().id().asLongText());
        System.out.println("handlerAdded 被调用" + ctx.channel().id().asShortText());
    }


    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        System.out.println("handlerRemoved 被调用" + ctx.channel().id().asLongText());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
