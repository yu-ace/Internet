package com.example.demo;

import java.net.Socket;

public class Session {
    String nickName;
    Socket socket;

    public Session() {
    }

    public Session(String nickName, Socket socket) {
        this.nickName = nickName;
        this.socket = socket;
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
