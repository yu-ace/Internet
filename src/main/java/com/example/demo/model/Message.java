package com.example.demo.model;

public class Message {
    String nickName;
    String receiverName;
    String message;

    public Message() {
    }

    public Message(String nickName, String receiverName, String message) {
        this.nickName = nickName;
        this.receiverName = receiverName;
        this.message = message;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
