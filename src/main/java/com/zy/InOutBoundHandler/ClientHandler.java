package com.zy.InOutBoundHandler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class ClientHandler extends SimpleChannelInboundHandler<Long> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Long msg) throws Exception {
        System.out.println("服务器的ip: " + ctx.channel().remoteAddress() + "\t消息\t" + msg);
    }

    //从写此方法发送数据
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("ClientHandler 发送数据");
        /*
         1."asdasdasasdasdas" 是16个字节
         2.该处理器的前一个handler 是 MyLongToByteEncoder
         3.MyLongToByteEncoder的父类是MessageToByteEncoder
         4. 父类  MessageToByteEncoder
         public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        ByteBuf buf = null;
        try {
            if (acceptOutboundMessage(msg)) { //判断当前msg 是不是应该处理的类型，如果是就处理，不是就跳过encode
                @SuppressWarnings("unchecked")
                I cast = (I) msg;
                buf = allocateBuffer(ctx, cast, preferDirect);
                try {
                    encode(ctx, cast, buf);
                } finally {
                    ReferenceCountUtil.release(cast);
                }

                if (buf.isReadable()) {
                    ctx.write(buf, promise);
                } else {
                    buf.release();
                    ctx.write(Unpooled.EMPTY_BUFFER, promise);
                }
                buf = null;
            } else {
                ctx.write(msg, promise);
            }
        }
        5. 因此我们编写 Encoder 是要注意传入的数据类型和处理的数据类型一致
         */
        //ctx.writeAndFlush(Unpooled.copiedBuffer("asdasdasasdasdas", CharsetUtil.UTF_8));
       ctx.writeAndFlush(123456L);//发送一个Long
    }
}
