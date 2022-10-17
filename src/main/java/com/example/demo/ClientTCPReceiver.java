package com.example.demo;

import com.alibaba.fastjson2.JSON;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public class ClientTCPReceiver implements Runnable {

    Socket socket;

    public ClientTCPReceiver(Socket socket) {
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
                if(message1.getNickName().equals("SYSTEM")){
                    System.out.printf("来自系统的消息： %s\n",message1.getMessage());
                }else {
                    System.out.printf("来自 %s 的消息 ： %s\n",message1.getNickName(),message1.getMessage());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}