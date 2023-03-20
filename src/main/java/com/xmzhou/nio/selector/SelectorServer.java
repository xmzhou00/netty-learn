package com.xmzhou.nio.selector;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

import static com.xmzhou.util.ByteBufferUtil.debugAll;

/**
 * NIO  采用IO多路复用的方式
 */
@Slf4j
public class SelectorServer {
    public static void main(String[] args) throws IOException {
        // 创建 selector 管理多个channel
        Selector selector = Selector.open();
        ByteBuffer buffer = ByteBuffer.allocate(16);
        try (ServerSocketChannel server = ServerSocketChannel.open()) {
            server.bind(new InetSocketAddress(9999));
            log.debug("init...");
            // 设置为非阻塞
            server.configureBlocking(false);
            // 将通道注册到选择器中，并设置感兴趣的事件
            server.register(selector, SelectionKey.OP_ACCEPT);
            while (true) {
                log.debug("before select...");
                // 如果没有事件，线程就会被阻塞，反之不会进行阻塞。避免CPU空转
                int count = selector.select();
                log.debug("selector event count: {}", count);
                // 获取所有事件
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                // 使用迭代器遍历事件
                Iterator<SelectionKey> iterator = selectionKeys.iterator();
                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();
                    if (key.isAcceptable()) {
                        // 获取key所对应的channel
                        ServerSocketChannel channel = (ServerSocketChannel) key.channel();
                        log.debug("before accept...");
                        SocketChannel socketChannel = channel.accept();
                        log.debug("after accept...");
                        socketChannel.configureBlocking(false);
                        socketChannel.register(selector, SelectionKey.OP_READ);
                        // 事件发生后，要么处理，要么取消
                        // 不能什么都不做，否则下次还会触发该事件
                    } else if (key.isReadable()) { // 获取可读事件
                        SocketChannel socketChannel = (SocketChannel) key.channel();
                        log.debug("before read...");
                        socketChannel.read(buffer);
                        buffer.flip();
                        debugAll(buffer);
                        log.debug("after read...");
                        buffer.clear();
                    }

                    iterator.remove();
                }
            }
        } catch (IOException e) {
        }
    }
}
