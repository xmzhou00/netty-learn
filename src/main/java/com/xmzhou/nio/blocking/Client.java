package com.xmzhou.nio.blocking;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;

/**
 * @Author: xianmingZhou
 * @Date: 2023/3/13 22:02
 * @Description:
 */
public class Client {
    public static void main(String[] args) throws IOException {

        SocketChannel channel = SocketChannel.open();
        channel.connect(new InetSocketAddress("localhost",9999));

        System.out.println("waitting...");
    }
}
