package com.xmzhou.nio;

import com.sun.javafx.scene.web.Debugger;

import java.nio.ByteBuffer;

import static com.xmzhou.util.ByteBufferUtil.debugAll;

/**
 * @Author: xianmingZhou
 * @Date: 2023/3/13 20:48
 * @Description:
 */
public class TestByteBuffer {
    public static void main(String[] args) {
        ByteBuffer buffer = ByteBuffer.allocate(10);
        // 向buffer中写入一个字节的数据
        buffer.put((byte) 97);
        buffer.put((byte) 98);

        debugAll(buffer);
        // 切换到读模式
        buffer.flip();
        debugAll(buffer);
        buffer.flip();
        debugAll(buffer);
        buffer.rewind();
        buffer.compact();
        debugAll(buffer);

    }
}
