package com.example.demo.server;

import com.alibaba.fastjson2.JSON;
import com.example.demo.Demo3Application;
import com.example.demo.model.Message;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.List;

public class Session implements Closeable, Runnable {
    String nickName;
    Socket socket;
    Thread thread;

    boolean isRunning = true;

    public Session() {
    }

    public Session(Socket socket) {
        this.socket = socket;
        thread = new Thread(this);
        thread.start();
        Message message2 = new Message("SYSTEM", "xxx", "欢迎使用QQ！");
        try {
            socket.getOutputStream().write(JSON.toJSONString(message2).getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void close() throws IOException {
        System.out.println("用户退出，关闭线程，释放资源");
        Demo3Application.sessionManager.removeSession(this);
        thread.interrupt();
        socket.close();
    }

    @Override
    public void run() {
        while (isRunning) {
            try {
                Message message1 = getMessage();
                if (message1.getReceiverName().equals("SYSTEM")) {
                    // 处理系统信息
                    handleCommand(message1);
                } else {
                    // 转发消息
                    messageTransfer(message1);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private void messageTransfer(Message message1) throws IOException {
        String receiverName = message1.getReceiverName();
        Session sessionByNickName = Demo3Application.sessionManager.getSessionByNickName(receiverName);

        if (sessionByNickName == null) {
            // 用户不在线 发送错误信息
            System.out.printf("来自%s 发送给%s 消息为: %s %s\n", message1.getNickName(),
                    receiverName, message1.getMessage(), "但用户不在线，发送失败");
            sendSystemMessage("用户不在线，发送失败", receiverName);
        } else {
            // 直接转发
            Socket receiveSocket = sessionByNickName.getSocket();
            sendMessage(message1, receiveSocket);
        }
    }

    private void handleCommand(Message message1) {
        if (message1.getMessage().equals("LIST")) {
            // 发送在线用户列表
            List<String> onlineList = Demo3Application.sessionManager.getOnlineList();
            String join = String.join("\n", onlineList);
            sendSystemMessage(join, message1.getNickName());
        } else if (message1.getMessage().equals("LOGIN")) {
            this.setNickName(message1.getNickName());
        } else if (message1.getMessage().equals("LOGOUT")) {
            isRunning = false;
            System.out.printf("用户%s退出\n", message1.getNickName());
            sendSystemMessage("BYE", message1.getNickName());
            try {
                this.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void sendSystemMessage(String message, String receiver) {
        Message message2 = new Message("SYSTEM", receiver, message);
        try {
            socket.getOutputStream().write(JSON.toJSONString(message2).getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void sendMessage(Message message, Socket socket) {
        try {
            socket.getOutputStream().write(JSON.toJSONString(message).getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Message getMessage() throws IOException {
        byte[] buffer = new byte[1024];
        InputStream in = socket.getInputStream();
        in.read(buffer);
        String message = new String(buffer);
        Message message1 = JSON.parseObject(message.trim(), Message.class);
        return message1;
    }
}
