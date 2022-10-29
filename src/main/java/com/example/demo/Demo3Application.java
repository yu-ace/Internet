package com.example.demo;

import com.example.demo.client.ClientHandler;
import com.example.demo.server.Session;
import com.example.demo.server.SessionManager;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

@SpringBootApplication
public class Demo3Application implements CommandLineRunner {

    static int ids = 0;

    public static int getId() {
        ids = ids + 1;
        return ids;
    }

    public static SessionManager sessionManager = SessionManager.getInstance();

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
            ClientHandler clientHandler = new ClientHandler();
            clientHandler.mainLoop();
        }
    }

    private void serverHandle(Scanner scanner) throws IOException {
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
}
