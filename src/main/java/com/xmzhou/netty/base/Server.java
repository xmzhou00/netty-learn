package com.xmzhou.netty.base;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;

/**
 * netty入门案例：Server端
 */
public class Server {
    public static void main(String[] args) {
        // 1.启动器，负责装配netty组件，启动服务器
        new ServerBootstrap()
                // 2、创建NioEventLoopGroup，可以简单理解为：线程池 + Selector
                .group(new NioEventLoopGroup())
                // 3、选择服务器的ServerSocketChannel实现
                .channel(NioServerSocketChannel.class)
                // 4、childHandler负责处理读写，该方法决定了child执行哪些操作
                // ChannelInitializer处理器（仅执行一次）
                // 它的作用是待客户端SocketChannel建立连接后，执行initChannel以便添加更多的处理器。
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel nioSocketChannel) throws Exception {
                        // 5、SocketChannel的处理器，使用StringDecoder解码
                        nioSocketChannel.pipeline().addLast(new StringDecoder());
                        // 6、SocketChannel的处理器，使用上一个处理器的处理结果
                        nioSocketChannel.pipeline().addLast(new SimpleChannelInboundHandler<String>() {
                            @Override
                            protected void channelRead0(ChannelHandlerContext channelHandlerContext, String s) throws Exception {
                                System.out.println(s);
                            }
                        });
                    }
                })
                // 7、绑定端口
                .bind(9999);
    }
}
