package com.xmzhou.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

import static com.xmzhou.util.ByteBufferUtil.debugAll;

/**
 * 充分利用多核cpu的性能
 * 设计两组选择器：
 * - 单线程配一个选择器（boss）：专门处理accept事件
 * - 创建cpu核心数的线程（worker）：每个线程配一个选择器，处理读写事件
 */
public class ThreadServer {
    public static void main(String[] args) {
        try (ServerSocketChannel server = ServerSocketChannel.open()) {
            Thread.currentThread().setName("boss");
            server.bind(new InetSocketAddress(9999));
            Selector boss = Selector.open();
            server.configureBlocking(false);
            server.register(boss, SelectionKey.OP_ACCEPT);
            // 创建固定数量的worker
            Worker[] workers = new Worker[4];
            AtomicInteger robin = new AtomicInteger(0);
            for (int i = 0; i < workers.length; i++) {
                workers[i] = new Worker("worker-" + i);
            }
            while (true) {
                boss.select();
                Set<SelectionKey> selectionKeys = boss.selectedKeys();
                Iterator<SelectionKey> iterator = selectionKeys.iterator();
                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();
                    iterator.remove();
                    if (key.isAcceptable()) {
                        // 建立连接
                        SocketChannel socket = server.accept();
                        socket.configureBlocking(false);
                        workers[robin.getAndIncrement() % workers.length].register(socket);
                    }
                }
            }
        } catch (IOException e) {
        }
    }


    static class Worker implements Runnable {
        private Thread thread;
        private volatile Selector selector;
        private String name;
        private volatile boolean started = false;

        /**
         * 同步队列，用于Boss线程和worker线程之间的通信
         */
        private ConcurrentLinkedQueue<Runnable> queue;

        public Worker(String name) {
            this.name = name;
        }

        /**
         * 注册
         *
         * @param socket
         * @throws IOException
         */
        public void register(final SocketChannel socket) throws IOException {
            if (!started) {
                synchronized (this) {
                    thread = new Thread(this, name);
                    selector = Selector.open();
                    queue = new ConcurrentLinkedQueue<>();
                    thread.start();
                    started = true;
                }
            }
            queue.add(() -> {
                try {
                    socket.register(selector, SelectionKey.OP_READ);
                } catch (ClosedChannelException e) {
                    throw new RuntimeException(e);
                }
            });
            selector.wakeup();
        }

        @Override
        public void run() {
            while (true) {
                try {
                    selector.select();
                    Runnable task = queue.poll();
                    if (task != null) {
                        // 获得任务 执行注册操作
                        task.run();
                    }
                    Set<SelectionKey> selectionKeys = selector.selectedKeys();
                    Iterator<SelectionKey> iterator = selectionKeys.iterator();
                    while (iterator.hasNext()) {
                        SelectionKey key = iterator.next();
                        iterator.remove();
                        // worker只负责读请求
                        if (key.isReadable()) {
                            SocketChannel channel = (SocketChannel) key.channel();
                            ByteBuffer buffer = ByteBuffer.allocate(16);
                            channel.read(buffer);
                            buffer.flip();
                            debugAll(buffer);
                        }
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
