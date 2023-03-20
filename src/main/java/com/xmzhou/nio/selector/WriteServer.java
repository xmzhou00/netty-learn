package com.xmzhou.nio.selector;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Set;

/**
 * NIO 可写事件
 */
@Slf4j
public class WriteServer {
    public static void main(String[] args) throws IOException {
        // 注册selector
        Selector selector = Selector.open();

        try (ServerSocketChannel server  = ServerSocketChannel.open()) {
            server.bind(new InetSocketAddress(9999));
            server.configureBlocking(false);
            // 绑定accept事件
            server.register(selector, SelectionKey.OP_ACCEPT);
            while (true) {
                log.debug("before...");
                int count = selector.select();
                log.debug("start handle event...");
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = selectionKeys.iterator();
                while (iterator.hasNext()){

                    SelectionKey key = iterator.next();
                    if (key.isAcceptable()) { // accept事件
//                       ServerSocketChannel channel = (ServerSocketChannel) key.channel();
//                        SocketChannel clientChannel = channel.accept();
                        SocketChannel clientChannel = server.accept();
                        log.debug("server connected...");
                        StringBuilder stringBuilder = new StringBuilder();
                        for (int i = 0; i < 5000; i++) {
                            stringBuilder.append("a");
                        }
                        ByteBuffer encodedStr = StandardCharsets.UTF_8.encode(stringBuilder.toString());
                        int write = clientChannel.write(encodedStr);
                        log.debug("accept write: {}",write);
                        if (encodedStr.hasRemaining()){
                            clientChannel.configureBlocking(false);
                            clientChannel.register(selector,SelectionKey.OP_WRITE,encodedStr);
                        }
                    }else if (key.isWritable()){
                       SocketChannel socket = (SocketChannel) key.channel();
                       ByteBuffer buffer = (ByteBuffer) key.attachment();
                       // 执行写操作
                        int write = socket.write(buffer);
                        log.debug("writable event write:{}",write);
                        if (!buffer.hasRemaining()){
                            key.attach(null);
                            key.interestOps(0);
                        }
                    }
                    iterator.remove();
                }
            }

        }


    }
}
