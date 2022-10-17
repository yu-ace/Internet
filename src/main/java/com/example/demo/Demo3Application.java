package com.example.demo;

import com.alibaba.fastjson2.JSON;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@SpringBootApplication
public class Demo3Application implements CommandLineRunner {

    static List<Session> sessionList = new ArrayList<>();

    public static void addSession(Session session){
        sessionList.add(session);
    }

    public static Session getSessionByNickName(String nickName){
        for (Session session : sessionList) {
            if(session.nickName.equals(nickName)){
                return session;
            }
        }
        return null;
    }


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
            while (true) {
                Socket connect = serverSocket.accept();
                System.out.println("用户连接");
                ServerTCPReceiver tcpReceiver = new ServerTCPReceiver(connect);
                new Thread(tcpReceiver).start();
            }

        }else {
            System.out.println("请输入服务器IP地址：");
            String ip = scanner.next();
            System.out.println("请输入监听的端口号：");
            int port = scanner.nextInt();
            System.out.println("请输入您的昵称：");
            String nickName = scanner.next();
            Socket socket = new Socket(ip,port);
            ClientTCPReceiver tcpReceiver = new ClientTCPReceiver(socket);
            new Thread(tcpReceiver).start();
            Message msg = new Message(nickName,"SYSTEM","LOGIN");
            byte[] messageBytes = JSON.toJSONString(msg).getBytes("utf8");
            socket.getOutputStream().write(messageBytes);
            while (true) {
                System.out.println("请输入需要接收消息的昵称：");
                String receiver = scanner.next();
                System.out.println("请输入需要发生的信息：");
                String message = scanner.next();
                Message msg2 = new Message(nickName,receiver,message);
                byte[] messageBytes2 = JSON.toJSONString(msg2).getBytes("utf8");
                socket.getOutputStream().write(messageBytes2);
            }
        }
    }
}
