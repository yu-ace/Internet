package com.example.demo.server;

import com.alibaba.fastjson2.JSON;
import com.example.demo.Demo3Application;
import com.example.demo.model.Message;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public class ServerTCPReceiver implements Runnable {

    Session session = null;

    public ServerTCPReceiver(Session session) {
        this.session = session;
    }

    @Override
    public void run() {
        while (true){
            try {
                Message message1 = getMessage();
                if(message1.getReceiverName().equals("SYSTEM")){
                    // 处理系统信息
                    handleCommand(message1);
                }else {
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

        if(sessionByNickName==null){
            // 用户不在线 发送错误信息
            System.out.printf("来自%s 发送给%s 消息为: %s %s\n", message1.getNickName(),
                    receiverName, message1.getMessage(),"但用户不在线，发送失败");
            sendSystemMessage("用户不在线，发送失败");
        }else{
            // 直接转发
            Socket receiveSocket = sessionByNickName.getSocket();
            sendMessage(message1,receiveSocket);
        }
    }

    private void handleCommand(Message message1) {
        if(message1.getMessage().equals("LIST")){
            // 发送在线用户列表
        }else if(message1.getMessage().equals("LOGIN")) {
            this.session.setNickName(message1.getNickName());
        }else if(message1.getMessage().equals("LOGOUT")) {
            System.out.printf("用户%s登录\n", message1.getNickName());
            sendSystemMessage("再见！");
            Demo3Application.sessionManager.removeSession(this.session);
        }
    }

    private void sendSystemMessage(String message){
        Message message2 = new Message("SYSTEM","xxx", message);
        try {
            session.getSocket().getOutputStream().write(JSON.toJSONString(message2).getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void sendMessage(Message message,Socket socket){
        try {
            socket.getOutputStream().write(JSON.toJSONString(message).getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Message getMessage() throws IOException {
        byte[] buffer = new byte[1024];
        Socket socket = session.getSocket();
        InputStream in = socket.getInputStream();
        in.read(buffer);
        String message = new String(buffer);
        Message message1 = JSON.parseObject(message.trim(), Message.class);
        return message1;
    }
}
