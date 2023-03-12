package com.xmzhou.bio;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @Author: xianmingZhou
 * @Date: 2023/3/12 22:12
 * @Description: BIO基本模板开发  server端
 */
@Slf4j
public class Server {
    public static void main(String[] args) {

        try {
            // 服务端注册端口
            log.debug("服务端启动");
            ServerSocket serverSocket = new ServerSocket(9999);
            // accept为阻塞操作，等待客户端的连接，得到一个socket管道
            Socket socket = serverSocket.accept();
            log.debug("服务端获得连接");
            // 获取流
            InputStream inputStream = socket.getInputStream();

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            log.debug("获取数据流..");
            String line;
            // readLine也是阻塞操作
            while ((line = bufferedReader.readLine())!=null){
                log.debug("收到数据 -- {}",line);
            }
            log.debug("结束数据流...");
        } catch (IOException e) {
           log.error("error");
        }

    }
}
