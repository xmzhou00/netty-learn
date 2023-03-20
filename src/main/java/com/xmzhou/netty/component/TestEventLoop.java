package com.xmzhou.netty.component;

import io.netty.channel.EventLoop;
import io.netty.channel.nio.NioEventLoopGroup;

import java.util.concurrent.TimeUnit;

/**
 * EventLoop:事件循环对象
 *  本质上是一个单线程执行器（同时维护了一个selector），里面有一个run方法处理一个或多个channel上的io事件
 *
 */
public class TestEventLoop {
    public static void main(String[] args) {
        // 默认线程数量是cpu核心数*2
        NioEventLoopGroup group = new NioEventLoopGroup(2);

        // 执行普通任务
        group.next().execute(()->{
            System.out.println(Thread.currentThread().getName()+"  hello");
        });

        // 执行定时任务
        group.next().scheduleAtFixedRate(()->{
            System.out.println(Thread.currentThread().getName()+" hello shceduler");
        },0,1, TimeUnit.SECONDS);

        /**
         * 优雅关闭。该方法会首先切换EventLoopGroup到关闭状态从而拒绝新任务的加入，然后在任务队列的 任务都完成后，停止线程的运行。
         * 从而确保整体应用是在正常有序状态下退出的。
         */
        group.shutdownGracefully(); // 优雅的关闭
    }
}
