package com.zy.webSocket;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.text.SimpleDateFormat;
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
        //ByteBuf byteBuf = Unpooled.copiedBuffer("服务器时间:" + sdf.format(new Date()) + "\t" + msg.text(), CharsetUtil.UTF_8);
        //ctx.channel().writeAndFlush(new TextWebSocketFrame(byteBuf));
        //ctx.channel().writeAndFlush(new TextWebSocketFrame("byteBuf"));
        System.out.println("channelRead0"+  ctx.channel().id().asLongText());


    }

//    public void write(String str){
//
//        ch.writeAndFlush(new TextWebSocketFrame(str));
//    }






    /**
     * 当web客户端连接后，就会触发
     * @param ctx
     * @throws Exception
     */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        //id表示唯一的值，LongText 是唯一的 ShortText不是唯一的
        Channel channel = ctx.channel();
        System.out.println("handlerAdded 被调用" + channel.id().asLongText());
        System.out.println("handlerAdded 被调用" + channel.id().asShortText());
        //当用户上线了 注册其通道
        channelGroup.add(channel);
        //返回用户的通道id
        ctx.channel().writeAndFlush(new TextWebSocketFrame(channel.id().asLongText()));
    }


    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        String longTex = ctx.channel().id().asLongText();
        System.out.println("handlerRemoved 被调用" + longTex);

    }



    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }


}
