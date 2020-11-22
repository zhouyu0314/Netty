package com.zy.groupChat;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.text.SimpleDateFormat;
import java.util.Date;

public class NettyServerHandler extends SimpleChannelInboundHandler<String> {

    //定义一个channel组，管理所有的channel
    private static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

    protected void channelRead0(ChannelHandlerContext channelHandlerContext, String msg) throws Exception {

        //获取当前channel
        Channel channel = channelHandlerContext.channel();
        //遍历channelGroup，根据不同的情况回复不同的消息
        //取出每一个channel
        channelGroup.forEach(ch -> {
            if (ch != channel) {//表示当前channel，直接转发
                ch.writeAndFlush("[客户]" + channel.remoteAddress() + "发送消息" + msg + "\n");
            } else {
                ch.writeAndFlush("[我]" + channel.remoteAddress() + "发送消息" + msg + "\n");
            }
        });

    }


    //handlerAdded -> channelActive -> channelInactive -> handlerRemoved

    //表示连接建立，一旦连接，第一个被执行
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        //将当前Channel加入到channelGroup
        Channel channel = ctx.channel();
        //将该客户加入聊天的信息推送给其他在线的客户端
        /*
        该方法会将channelGroup中所有的channel遍历，并发送消息
         */
        channelGroup.writeAndFlush("[客户端]" + channel.remoteAddress() + "加入聊天" + "\t" + sdf.format(new Date()) + "\n");
        channelGroup.add(channel);
        System.out.println("handlerAdded channelGroup size:" + channelGroup.size());

    }

    //断开连接会被触发，并且会将当前的channel自动从channelGroup中删除
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        channelGroup.writeAndFlush("[客户端]" + channel.remoteAddress() + "离开" + "\t" + sdf.format(new Date()) + "\n");
        System.out.println("handlerRemoved channelGroup size:" + channelGroup.size());
    }

    //表示channel处于活动状态，提示xx上线
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().remoteAddress() + "上线了" + "\t" + sdf.format(new Date()));
    }

    //表示channel处于非活动状态，提示xx下线
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().remoteAddress() + "下线了" + "\t" + sdf.format(new Date()));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
