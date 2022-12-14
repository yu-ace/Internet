package com.example.demo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

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
            ReceiverThread t = new ReceiverThread(port);
            new Thread(t).start();
            while (true) {
                System.out.println("请输入需要发生的信息：");
                String message = scanner.next();
                InetSocketAddress address = new InetSocketAddress(t.getAddress().getHostAddress() ,65525);
                byte[] messageBytes = message.getBytes("utf8");
                DatagramPacket sendPacket = new DatagramPacket(messageBytes, messageBytes.length, address);
                DatagramSocket datagramSocket = new DatagramSocket();
                datagramSocket.send(sendPacket);
            }
        }else {
            System.out.println("请输入服务器IP地址：");
            String ip = scanner.next();
            System.out.println("请输入监听的端口号：");
            int port = scanner.nextInt();
            ReceiverThread t = new ReceiverThread(65525);
            new Thread(t).start();
            while (true) {
                System.out.println("请输入需要发生的信息：");
                String message = scanner.next();
                InetSocketAddress address = new InetSocketAddress(ip, port);
                byte[] messageBytes = message.getBytes("utf8");
                DatagramPacket sendPacket = new DatagramPacket(messageBytes, messageBytes.length, address);
                DatagramSocket datagramSocket = new DatagramSocket();
                datagramSocket.send(sendPacket);
            }
        }
    }
}
