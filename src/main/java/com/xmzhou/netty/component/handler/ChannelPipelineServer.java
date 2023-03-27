package com.xmzhou.netty.component.handler;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * @Author: xianmingZhou
 * @Date: 2023/3/21 20:34
 * @Description:  channelInboundHandlerAdapter按照addList顺序执行，而channelOutboundHandlerAdapter按照addLast逆序执行
 */
public class ChannelPipelineServer {
    public static void main(String[] args) {

        new ServerBootstrap()
                .group(new NioEventLoopGroup())
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel channel) throws Exception {
                        channel.pipeline().addLast(new ChannelInboundHandlerAdapter(){
                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                System.out.println("1");
                                ctx.fireChannelRead(msg);
                            }
                        });

                        channel.pipeline().addLast(new ChannelInboundHandlerAdapter(){
                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                System.out.println("2");
                                ctx.fireChannelRead(msg);
                            }
                        });
                        /**
                         * ctx.channel().write()和ctx.write()对比：
                         *  都是触发出站处理器的执行
                         *  ctx.channel().write()从尾部开始查找
                         *  ctx.write()是从当前节点找上一个出站处理器
                         */
                        channel.pipeline().addLast(new ChannelInboundHandlerAdapter(){
                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                System.out.println("3");
                                ctx.channel().write(msg); // 从pipeline尾部开始触发执行
                            }
                        });
                        channel.pipeline().addLast(new ChannelOutboundHandlerAdapter(){
                            @Override
                            public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
                                System.out.println("4");
                                ctx.write(msg,promise);
                            }
                        });

                        channel.pipeline().addLast(new ChannelOutboundHandlerAdapter(){
                            @Override
                            public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
                                System.out.println("5");
                                ctx.write(msg,promise);
                            }
                        });
                    }
                }).bind(9999);

    }
}
