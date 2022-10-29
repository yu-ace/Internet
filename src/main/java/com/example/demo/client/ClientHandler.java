package com.example.demo.client;

import com.alibaba.fastjson2.JSON;
import com.example.demo.model.Message;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class ClientHandler {

    public static int userId = -1;
    Scanner scanner = new Scanner(System.in);
    ClientTCPReceiver tcpReceiver;
    Socket socket;
    Thread thread;

    public void mainLoop() throws Exception {
        clientHandle();
    }


    private void clientHandle() throws Exception {
        System.out.println("请输入服务器IP地址：");
        String ip = scanner.next();
        System.out.println("请输入监听的端口号：");
        int port = scanner.nextInt();
        System.out.println("请输入您的昵称：");
        String nickName = scanner.next();

        socket = new Socket(ip, port);
        tcpReceiver = new ClientTCPReceiver(socket);
        thread = new Thread(tcpReceiver);
        thread.start();
        Message message = new Message();
        message.setCommand("LOGIN");
        message.setNickName(nickName);
        sendCmd2Server(message);
        while (userId == -1) {
            System.out.println("等待服务器返回注册信息，请稍等...");
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        while (true) {
            printHelp();
            String cmd = scanner.next();
            switch (cmd) {
                case "1":
                    Message message2 = new Message();
                    message2.setUserId(userId);
                    message2.setCommand("LIST_ROOM");
                    sendCmd2Server(message2);
                    break;
                case "2":
                    System.out.println("请输入房间名称");
                    String name = scanner.next();
                    System.out.println("请输入房间描述");
                    String desc = scanner.next();
                    Message message3 = new Message();
                    message3.setUserId(userId);
                    message3.setCommand("NEW_ROOM");
                    message3.setNickName(name);
                    message3.setContent(desc);
                    sendCmd2Server(message3);
                    break;
                case "3":
                    System.out.println("请输入房间ID");
                    int roomId = scanner.nextInt();
                    Message message4 = new Message();
                    message4.setUserId(userId);
                    message4.setCommand("JOIN_ROOM");
                    message4.setRoomId(roomId);
                    sendCmd2Server(message4);

                    roomLoop(roomId);
                    break;
                case "4":
                    getBalance();
                    break;
                case "5":
                    getTransactions();
                    break;
                case "q":
                    Message message6 = new Message();
                    message6.setUserId(userId);
                    message6.setCommand("LOGOUT");
                    sendCmd2Server(message6);
                    System.exit(0);
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + cmd);
            }
        }
    }

    private void getTransactions() throws IOException {
        Message message7 = new Message();
        message7.setUserId(userId);
        message7.setCommand("GET_LIST");
        sendCmd2Server(message7);
    }

    private void getBalance() throws IOException {
        Message message5 = new Message();
        message5.setUserId(userId);
        message5.setCommand("GET_BALANCE");
        sendCmd2Server(message5);
    }

    private void roomLoop(int roomId) throws Exception {
        while (true) {
            printRoomHelp();
            String cmd = scanner.next();
            switch (cmd) {
                case "1":
                    Message message5 = new Message();
                    message5.setRoomId(roomId);
                    message5.setCommand("NEW_GAME");
                    sendCmd2Server(message5);
                    break;
                case "2":
                    System.out.println("请输入游戏ID");
                    int gameId = scanner.nextInt();
                    System.out.println("请输入游戏大小，-1小 1大");
                    int result = scanner.nextInt();
                    System.out.println("请输入赌注");
                    int amount = scanner.nextInt();
                    Message message6 = new Message();
                    message6.setGameId(gameId);
                    message6.setCommand("JOIN_GAME");
                    message6.setResult(result);
                    message6.setAmount(amount);
                    sendCmd2Server(message6);
                    break;
                case "3":
                    System.out.println("请输入游戏ID");
                    int gameId2 = scanner.nextInt();
                    Message message7 = new Message();
                    message7.setGameId(gameId2);
                    message7.setCommand("END_GAME");
                    sendCmd2Server(message7);
                    break;
                case "4":
                    Message message8 = new Message();
                    message8.setRoomId(roomId);
                    message8.setCommand("LAST_GAME");
                    sendCmd2Server(message8);
                    break;
                case "5":
                    getBalance();
                    break;
                case "6":
                    getTransactions();
                    break;
            }
        }
    }

    private void printRoomHelp() {
        System.out.println("输入1： 创建游戏");
        System.out.println("输入2： 下注");
        System.out.println("输入3： 结束游戏");
        System.out.println("输入4： 查看正在进行的游戏");
        System.out.println("输入5： 查询余额");
        System.out.println("输入6： 查询交易信息");
        System.out.println("输入q： 退出房间");
    }


    private void sendCmd2Server(Message message) throws IOException {
        byte[] messageBytes = JSON.toJSONString(message).getBytes();
        socket.getOutputStream().write(messageBytes);
    }


    private void printHelp() {
        System.out.println("输入1：查看所有的房间");
        System.out.println("输入2：坐庄，创建房间");
        System.out.println("输入3：进入房间");
        System.out.println("输入4：查看余额");
        System.out.println("输入5：查看我的交易记录");
        System.out.println("输入q：退出");
    }
}
