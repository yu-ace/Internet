package com.example.demo;

import com.alibaba.fastjson2.JSON;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public class ServerTCPReceiver implements Runnable {

    Socket socket;

    public ServerTCPReceiver(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        while (true){
            byte[] buffer = new byte[1024];
            try {
                InputStream in = socket.getInputStream();
                in.read(buffer);
                String message = new String(buffer);
                Message message1 = JSON.parseObject(message.trim(), Message.class);
                if(message1.getReceiverName().equals("SYSTEM")){
                    Session session = new Session(message1.getNickName(),socket);
                    Demo3Application.addSession(session);
                }else {
                    String receiverName = message1.getReceiverName();
                    Session sessionByNickName = Demo3Application.getSessionByNickName(receiverName);
                    if(sessionByNickName==null){
                        System.out.printf("来自%s 发送给%s 消息为: %s %s\n", message1.getNickName(),
                                receiverName,message1.getMessage(),"但用户不在线，发送失败");
                        Message message2 = new Message("SYSTEM","xxx","用户不在线，发送失败");
                        socket.getOutputStream().write(JSON.toJSONString(message2).getBytes("utf8"));
                    }else{
                        Socket socket1 = sessionByNickName.getSocket();
                        socket1.getOutputStream().write(JSON.toJSONString(message1).getBytes("utf8"));
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}
