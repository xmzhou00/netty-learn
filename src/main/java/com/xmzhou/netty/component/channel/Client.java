package com.xmzhou.netty.component.channel;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;

/**
 * @Author: xianmingZhou
 * @Date: 2023/3/20 22:06
 * @Description: channel的相关测试
 */
public class Client {
    public static void main(String[] args) throws InterruptedException {
        // 此处返回的是channelFuture对象，它的作用是利用channel()方法来获取Channel对象
        ChannelFuture channelFuture = new Bootstrap()
                .group(new NioEventLoopGroup())
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<Channel>() {
                    @Override
                    protected void initChannel(Channel channel) throws Exception {
                        channel.pipeline().addLast(new StringEncoder());
                    }
                })
                /**
                 * connect是异步的，这意味着它不等连接建立好后就直接返回了。因此ChannelFuture对象中不能立即获取到正确的Channel对象
                 */
                .connect("localhost", 9999);
        System.out.println(channelFuture.channel()); // [id: 0x12b9a91b]
        /**
         * 同步等待连接建立
         */
        // channelFuture.sync();

        // 除了sync，还可以基于回调的方式
        channelFuture.addListener((ChannelFutureListener)future ->{
            System.out.println(future.channel());
        });

        // System.out.println(channelFuture.channel()); //[id: 0x12b9a91b, L:/127.0.0.1:49877 - R:localhost/127.0.0.1:9999] 此时才正确获取到
        // .channel()
        // .writeAndFlush(new Date()+": hello world");
    }
}
