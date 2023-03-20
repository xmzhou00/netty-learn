package com.xmzhou.nio.nonblocking;

import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;

import static com.xmzhou.util.ByteBufferUtil.debugAll;

/**
 * NIO 非阻塞方式 Server端
 * 问题：
 *      设置了非阻塞，会一直执行while(true)中的代码，CPU一直处于空转状态，会使得性能降低
 */
@Slf4j
public class Server {
    public static void main(String[] args) {
        log.debug("init...");
        // 创建缓冲区
        ByteBuffer buffer = ByteBuffer.allocate(16);
            // 获取通道
        try (ServerSocketChannel server = ServerSocketChannel.open()) {
            server.bind(new InetSocketAddress(9999));
            // 存放连接的集合
            ArrayList<SocketChannel> channels = new ArrayList<>();

            while (true){
                // 设置为非阻塞模式
                server.configureBlocking(false);
                // 非阻塞模式下，如果没有连接，会返回null，不会阻塞
                SocketChannel socketChannel = server.accept();
                if (socketChannel !=null){
                    log.debug("connected...");
                    channels.add(socketChannel);
                }

                for (SocketChannel channel : channels) {
                    // 处理通道中的数据
                    // 设置为非阻塞模式下，若通道中没有数据，会返回0，不会阻塞线程
                    channel.configureBlocking(false);
//                    log.debug("before read...");
                    int size = channel.read(buffer);
                    if (size>0){
                        buffer.flip();
                        debugAll(buffer);
                        buffer.clear();
//                        log.debug("after read...");
                    }
                }

            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
