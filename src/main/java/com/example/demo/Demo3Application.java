package com.example.demo;

import com.alibaba.fastjson2.JSON;
import com.example.demo.client.ClientTCPReceiver;
import com.example.demo.model.Message;
import com.example.demo.server.Session;
import com.example.demo.server.SessionManager;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.net.*;
import java.util.Scanner;

@SpringBootApplication
public class Demo3Application implements CommandLineRunner {

    public static SessionManager sessionManager = null;

    public static void main(String[] args) {
        SpringApplication.run(Demo3Application.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        System.out.println("请输入模式：1服务端 2客户端");
        String next = scanner.next();
        if("1".equals(next)) {
            serverHandle(scanner);
        }else {
            clientHandle(scanner);
        }
    }

    private static void serverHandle(Scanner scanner) throws IOException {
        sessionManager = new SessionManager();
        System.out.println("请输入监听的端口号：");
        int port = scanner.nextInt();

        ServerSocket serverSocket = new ServerSocket(port);
        while (true) {
            Socket connect = serverSocket.accept();
            System.out.println("用户连接");
            Session session = new Session(connect);
            sessionManager.addSession(session);
        }
    }

    private static void clientHandle(Scanner scanner) throws IOException {
        System.out.println("请输入服务器IP地址：");
        String ip = scanner.next();
        System.out.println("请输入监听的端口号：");
        int port = scanner.nextInt();
        System.out.println("请输入您的昵称：");
        String nickName = scanner.next();

        Socket socket = new Socket(ip,port);
        ClientTCPReceiver tcpReceiver = new ClientTCPReceiver(socket);
        new Thread(tcpReceiver).start();
        sendMessage(nickName, "SYSTEM", "LOGIN", socket);
        while (true) {
            System.out.println("请输入需要接收消息的昵称：");
            String receiver = scanner.next();
            System.out.println("请输入需要发生的信息：");
            String message = scanner.next();
            sendMessage(nickName, receiver, message, socket);
        }
    }

    private static void sendMessage(String nickName, String receiver, String message, Socket socket) throws IOException {
        Message msg = new Message(nickName, receiver, message);
        byte[] messageBytes = JSON.toJSONString(msg).getBytes();
        socket.getOutputStream().write(messageBytes);
    }
}
