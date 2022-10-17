package com.example.demo.server;

import com.alibaba.fastjson2.JSON;
import com.example.demo.model.Message;
import com.example.demo.server.ServerTCPReceiver;

import java.io.IOException;
import java.net.Socket;

public class Session {
    String nickName;
    Socket socket;

    ServerTCPReceiver tcpReceiver;

    public Session() {
    }

    public Session(Socket socket) {
        this.socket = socket;
        tcpReceiver = new ServerTCPReceiver(this);
        new Thread(tcpReceiver).start();
        Message message2 = new Message("SYSTEM","xxx", "欢迎使用QQ！");
        try {
            socket.getOutputStream().write(JSON.toJSONString(message2).getBytes("utf8"));
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
}
