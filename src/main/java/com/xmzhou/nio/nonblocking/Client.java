package com.xmzhou.nio.nonblocking;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;

/**
 * 非阻塞模式 Client
 */
public class Client {
    public static void main(String[] args) {
        try (SocketChannel socketChannel = SocketChannel.open()) {
            // 与服务端建立连接
            socketChannel.connect(new InetSocketAddress("localhost",9999));

            System.out.println("waiting...");
        } catch (IOException e) {
        }

    }
}
