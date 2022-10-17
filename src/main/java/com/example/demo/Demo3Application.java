package com.example.demo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.InputStream;
import java.net.*;
import java.util.Scanner;
import java.util.concurrent.ThreadPoolExecutor;

@SpringBootApplication
public class Demo3Application implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(Demo3Application.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        System.out.println("请输入模式：1服务端 2客户端");
        String next = scanner.next();
        if("1".equals(next)) {
            System.out.println("请输入监听的端口号：");
            int port = scanner.nextInt();
            ServerSocket serverSocket = new ServerSocket(port);
            Socket connect = serverSocket.accept();
            TCPReceiver tcpReceiver = new TCPReceiver(connect);
            new Thread(tcpReceiver).start();
            while (true) {
                System.out.println("请输入需要发生的信息：");
                String message = scanner.next();
                byte[] messageBytes = message.getBytes("utf8");
                connect.getOutputStream().write(messageBytes);
            }
        }else {
            System.out.println("请输入服务器IP地址：");
            String ip = scanner.next();
            System.out.println("请输入监听的端口号：");
            int port = scanner.nextInt();
            System.out.println("请输入您的昵称：");
            String nickName = scanner.next();
            Socket socket = new Socket(ip,port);
            TCPReceiver tcpReceiver = new TCPReceiver(socket);
            new Thread(tcpReceiver).start();
            while (true) {
                System.out.println("请输入需要发生的信息：");
                String message = scanner.next();
                byte[] messageBytes = message.getBytes("utf8");
                socket.getOutputStream().write(messageBytes);
            }
        }
    }
}
