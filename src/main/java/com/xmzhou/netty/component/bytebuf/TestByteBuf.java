package com.xmzhou.netty.component.bytebuf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

import static io.netty.buffer.ByteBufUtil.appendPrettyHexDump;
import static io.netty.util.internal.StringUtil.NEWLINE;

/**
 * @Author: xianmingZhou
 * @Date: 2023/3/21 20:54
 * @Description:
 */
public class TestByteBuf {
    public static void main(String[] args) {
        ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer(10);
        log(buffer);

        ByteBufAllocator.DEFAULT.heapBuffer(10); // 创建堆内存
        ByteBufAllocator.DEFAULT.directBuffer(10); // 直接内存
    }


    private static void log(ByteBuf buffer) {
        int length = buffer.readableBytes();
        int rows = length / 16 + (length % 15 == 0 ? 0 : 1) + 4;
        StringBuilder buf = new StringBuilder(rows * 80 * 2)
                .append("read index:").append(buffer.readerIndex())
                .append(" write index:").append(buffer.writerIndex())
                .append(" capacity:").append(buffer.capacity())
                .append(NEWLINE);
        appendPrettyHexDump(buf, buffer);
        System.out.println(buf.toString());
    }
}
