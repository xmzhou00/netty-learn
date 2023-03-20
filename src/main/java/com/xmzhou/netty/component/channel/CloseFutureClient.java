package com.xmzhou.netty.component.channel;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.Scanner;

/**
 * @Author: xianmingZhou
 * @Date: 2023/3/20 22:23
 * @Description: 处理 channel.close事件
 */
@Slf4j
public class CloseFutureClient {
    public static void main(String[] args) throws InterruptedException {
        NioEventLoopGroup group = new NioEventLoopGroup();
        ChannelFuture channelFuture = new Bootstrap()
                .group(group)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    @Override // 在连接建立之后执行
                    protected void initChannel(NioSocketChannel channel) throws Exception {
                        log.debug("init channel...");
                        channel.pipeline().addLast(new LoggingHandler(LogLevel.DEBUG));
                        channel.pipeline().addLast(new StringEncoder());
                    }
                })
                .connect(new InetSocketAddress("localhost", 9999));

        Channel channel = channelFuture.sync().channel();
        log.debug("{}",channel);
        new Thread(()->{
            Scanner scanner = new Scanner(System.in);
            while (true) {
            String line = scanner.nextLine();
                if ("q".equals(line)){
                    // close是一个异步操作。真正的关闭操作并不是在调用该方法的线程中执行的，而是在NIO线程中执行的。
                    channel.close();
                    // log.debug("处理关闭之后的操作");
                    return;
                }
                channel.writeAndFlush(line);
            }
        },"input").start();

        ChannelFuture closeFuture = channel.closeFuture();
        closeFuture.addListener((ChannelFutureListener) future -> {
            log.debug("处理关闭后的操作");
            group.shutdownGracefully();
        });

    }
}
