package com.xmzhou.nio.blocking;

import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

import static com.xmzhou.util.ByteBufferUtil.debugAll;

/**
 * @Author: xianmingZhou
 * @Date: 2023/3/13 21:53
 * @Description: NIO 阻塞操作 Server端
 */
@Slf4j
public class Server {
    public static void main(String[] args) {
        // 创建缓冲区
        ByteBuffer buffer = ByteBuffer.allocate(16);
        // 获得服务器通道
        try (ServerSocketChannel server = ServerSocketChannel.open()) {
            // 为通道绑定端口
            server.bind(new InetSocketAddress(9999));
            while (true){
                log.debug("before connected...");
                // 没有连接的时候，会阻塞
                SocketChannel channel = server.accept();
                log.debug("after connected...");

                // 读取数据
                log.debug("before read...");
                channel.read(buffer);
                buffer.flip();
                debugAll(buffer);
                buffer.clear();
                log.debug("after read...");

                // 如果第一个client端发送一次数据后，下一次就不能发送数据了，因为会在 server.accept()处进行阻塞。
            }

        } catch (Exception e) {

        }
    }
}
