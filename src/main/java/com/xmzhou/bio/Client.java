package com.xmzhou.bio;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;

/**
 * @Author: xianmingZhou
 * @Date: 2023/3/12 22:13
 * @Description: BIO基本模板开发  Client端
 */
public class Client {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("localhost", 9999);
        OutputStream outputStream = socket.getOutputStream();
        // PrintStream printStream = new PrintStream(outputStream);
        // printStream.println("hello,I'm Client");
    }
}
