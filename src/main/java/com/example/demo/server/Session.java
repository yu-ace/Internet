package com.example.demo.server;

import com.alibaba.fastjson2.JSON;
import com.example.demo.Demo3Application;
import com.example.demo.model.Message;
import com.example.demo.server.ServerTCPReceiver;

import java.io.Closeable;
import java.io.IOException;
import java.net.Socket;

public class Session implements Closeable {
    String nickName;
    Socket socket;

    ServerTCPReceiver tcpReceiver;
    Thread thread;

    public Session() {
    }

    public Session(Socket socket) {
        this.socket = socket;
        tcpReceiver = new ServerTCPReceiver(this);
        thread = new Thread(tcpReceiver);
        thread.start();
        Message message2 = new Message("SYSTEM","xxx", "欢迎使用QQ！");
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
}
