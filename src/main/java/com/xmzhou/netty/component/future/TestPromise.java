package com.xmzhou.netty.component.future;

import io.netty.channel.DefaultEventLoop;
import io.netty.channel.DefaultEventLoopGroup;
import io.netty.util.concurrent.DefaultPromise;
import io.netty.util.concurrent.Promise;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutionException;

/**
 * @Author: xianmingZhou
 * @Date: 2023/3/20 22:55
 * @Description: netty 中的 future和promise
 */
@Slf4j
public class TestPromise {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        /**
         * netty中的Future和jdk中的Future同名，但是他们是两个接口，netty的Future继承自jdk的Future
         * 而Promise又是对netty的Future进行了扩展。
         */
        // * jdk Future 只能同步等待任务结束（或成功、或失败）才能得到结果
        // * netty Future 可以同步等待任务结束得到结果，也可以异步方式得到结果，但都是要等任务结束
        // * netty Promise 不仅有 netty Future 的功能，而且脱离了任务独立存在，只作为两个线程间传递结果的容器


        DefaultEventLoop eventExecutors = new DefaultEventLoop();
        // Promise相当于一个容器，可以存放各个线程中的结果，然后让其他线程去获取该结果
        DefaultPromise<Integer> promise = new DefaultPromise<>(eventExecutors);

        // 案例1 —— 同步处理任务
        // eventExecutors.execute(()->{
        //     try {
        //         Thread.sleep(1000);
        //     } catch (InterruptedException e) {
        //         throw new RuntimeException(e);
        //     }
        //     promise.setSuccess(10);
        // });
        //
        // log.debug("start...");
        // log.debug("{}",promise.getNow()); // null
        // log.debug("{}",promise.get());


        // 案例2 —— 异步处理任务
        promise.addListener(future ->{
            // 这里的future就是上面的promise
            log.debug("{}",future.getNow());
        });

        eventExecutors.execute(()->{
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            log.debug("set success, {}",10);
            promise.setSuccess(10);

        });

        log.debug("start...");





    }
}
