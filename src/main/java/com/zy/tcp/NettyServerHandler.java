package com.zy.tcp;

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
        //*****************************v3 简易版聊天室*************************************

        //*****************************v2*************************************
        /*
        若是此时有一个非常耗时的业务 -> 异步执行 -> 提交该channel对应的NioEventLoop到taskQueue中
         */
        //解决方案1 用户程序自定义普通任务
//        ctx.channel().eventLoop().execute(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    Thread.sleep(10 * 1000);
//                    ctx.writeAndFlush(Unpooled.copiedBuffer("Hello，客户端～（taskQueue）1", CharsetUtil.UTF_8));
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//
//            }
//
//        });
//        //一个taskQueue 同一个线程，下面会在30秒后输出
//        ctx.channel().eventLoop().execute(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    Thread.sleep(20 * 1000);
//                    ctx.writeAndFlush(Unpooled.copiedBuffer("Hello，客户端～（taskQueue）2", CharsetUtil.UTF_8));
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//
//            }
//
//        });


        //解决方案2 用户程序自定义定时任务 -> 该任务是提交到scheduleTaskQueue中
        //schedule() 只触发一次
        //scheduleAtFixedRate();上一个任务开始的时间计时，period时间过去后，检测上一个任务是否执行完毕，如果上一个任务执行完毕，则当前任务立即执行，如果上一个任务没有执行完毕，则需要等上一个任务执行完毕后立即执行。
        //scheduleWithFixedDelay();上一个任务结束时开始计时，period时间过去后，立即执行
//        ctx.channel().eventLoop().schedule(new Runnable() {
//            @Override
//            public void run() {
//                    ctx.writeAndFlush(Unpooled.copiedBuffer("Hello，客户端～（scheduleTaskQueue）1", CharsetUtil.UTF_8));
//            }
//
//        },5,TimeUnit.SECONDS);
//
//        System.out.println("go on ...");

        //*****************************v1*************************************
//        System.out.println("服务器读取线程信息：" + Thread.currentThread().getName());
//        System.out.println("查看Channel和pipeline的关系");
//        Channel channel = ctx.channel();
//        ChannelPipeline pipeline = ctx.pipeline();//本质是一个双向链表
//
//
//        //将msg转换成ByteBuffer
//        //此ByteBuf是netty提供的，性能更高！
//        ByteBuf buf = (ByteBuf) msg;
//        System.out.println("客户端发送的消息是：" + buf.toString(CharsetUtil.UTF_8));
//        System.out.println("客户端地址：" + channel.remoteAddress());
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
