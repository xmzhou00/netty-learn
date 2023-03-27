package com.xmzhou.netty.component.handler;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;

/**
 * @Author: xianmingZhou
 * @Date: 2023/3/21 20:40
 * @Description:
 */
public class Client {
    public static void main(String[] args) {
        new Bootstrap()
                .group(new NioEventLoopGroup())
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<Channel>() {
                    @Override
                    protected void initChannel(Channel channel) throws Exception {
                        channel.pipeline().addLast(new StringEncoder());
                    }
                })
                .connect("localhost", 9999)
                .addListener((ChannelFutureListener) future -> {
                    future.channel().writeAndFlush("hello world");
                });
    }
}
