package com.xmzhou.nio;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @Author: xianmingZhou
 * @Date: 2023/3/13 20:30
 * @Description: bytebuffer测试
 */
public class TestFileChannel {
    public static void main(String[] args) throws Exception {
        // 获取FileChannel
        FileChannel fileChannel = new FileInputStream("data/a.txt").getChannel();
        // 申请缓冲区
        ByteBuffer buffer = ByteBuffer.allocate(5);

        int hasNext = 0;
        StringBuilder str = new StringBuilder();
        while ((hasNext = fileChannel.read(buffer)) > 0) {
            // 切换到读模式
            buffer.flip();
            while (buffer.hasRemaining()) {
                str.append((char) buffer.get());
            }
            buffer.clear();
        }
        System.out.println(str);
    }
}
