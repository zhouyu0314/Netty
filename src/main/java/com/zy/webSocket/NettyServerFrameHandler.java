package com.zy.webSocket;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.CharsetUtil;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * TextWebSocketFrame 类型，表示一个文本帧（Frame）
 * 范型的内容是客户端与服务端交互的内容的类型
 */
public class NettyServerFrameHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
    private static Map<String,String> users = new HashMap<>();
    //定义一个channel组，管理所有的channel
    private static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);





    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        System.out.println("服务器端收到消息：" + msg.text());
        //回复浏览器
        ByteBuf byteBuf = Unpooled.copiedBuffer("服务器时间:" + sdf.format(new Date()) + "\t" + msg.text(), CharsetUtil.UTF_8);
        ctx.channel().writeAndFlush(new TextWebSocketFrame(byteBuf));

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
//        users.remove(ctx.channel().id().asLongText());
//        ByteBuf byteBuf = Unpooled.copiedBuffer(JSON.toJSONString(users), CharsetUtil.UTF_8);
//        ctx.writeAndFlush(new TextWebSocketFrame(byteBuf));
    }



    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }


}
