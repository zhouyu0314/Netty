package com.zy.buf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.util.CharsetUtil;

public class NettyByteBufDemo02 {
    public static void main(String[] args) {
        //创建ByteBuf
        ByteBuf byteBuf = Unpooled.copiedBuffer("Hello,world!", CharsetUtil.UTF_8);
        //使用相关的方法
        if (byteBuf.hasArray()) {
            byte[] content = byteBuf.array();

            System.out.println(new String(content, CharsetUtil.UTF_8));
            System.out.println("byteBuf:" + byteBuf);

            System.out.println(byteBuf.arrayOffset()); // 0
            System.out.println(byteBuf.readerIndex()); // 0
            System.out.println(byteBuf.writerIndex()); // 12

            System.out.println(byteBuf.capacity()); // 36
            System.out.println("可读取的大小"+byteBuf.readableBytes()); // 12

            for (int i = 0; i < byteBuf.readableBytes(); i++) {
                System.out.print((char) byteBuf.getByte(i));
            }
            System.out.println("");

            //区间读取
            System.out.println(byteBuf.getCharSequence(0, 5, CharsetUtil.UTF_8));
            System.out.println(byteBuf.arrayOffset()); // 0
            System.out.println(byteBuf.readerIndex()); // 0
            System.out.println(byteBuf.writerIndex()); // 12
            System.out.println(byteBuf.readCharSequence(5, CharsetUtil.UTF_8));
            System.out.println(byteBuf.arrayOffset()); // 0
            System.out.println(byteBuf.readerIndex()); // 5
            System.out.println(byteBuf.writerIndex()); // 12
        }

    }

}
