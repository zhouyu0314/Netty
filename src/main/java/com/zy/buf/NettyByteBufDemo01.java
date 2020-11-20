package com.zy.buf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * Netty 提供一个专门用来操作缓冲区(即Netty的数据容器)的工具类
 * 通过给定的数据和字符编码返回一个 ByteBuf 对象（类似于 NIO 中的 ByteBuffer 但有区别）
 * public static ByteBuf copiedBuffer(CharSequence string, Charset charset)
 */
public class NettyByteBufDemo01 {
    public static void main(String[] args) {
        //创建一个ByteBuf
        /*
        说明
        1.创建 对象 ，该对象包含一个byte类型的数组 长度10  byte[10]
        2.在netty的ByteBuf，不需要使用flip进行反转，其底层维护了readerIndex和writerIndex
        3.通过readerIndex 、 writerIndex 和 capacity 将ByteBuf分成三段

        0---readerIndex 已读取区域
        readerIndex---writerIndex 可读区域

        0---writerIndex 已写区域
        writerIndex---capacity 可写区域

         */
        ByteBuf buffer = Unpooled.buffer(10);

        for (int i = 0; i < 10; i++) {
            buffer.writeByte(i);
        }
        System.out.println("capacity:" + buffer.capacity());
        //输出
        for (int i = 0; i < buffer.capacity(); i++) {
            //System.out.println(buffer.getByte(i));//指定index之后readerIndex不会变化
            System.out.println(buffer.readByte());// //每读一次readerIndex+1
        }

    }
}
