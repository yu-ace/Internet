package com.example.demo;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class ReceiverThread implements Runnable{
    int port = 2022;
    InetAddress address;

    public ReceiverThread(int port) {
        this.port = port;
    }

    @Override
    public void run() {
        DatagramSocket mSocket = null;
        try {
            mSocket = new DatagramSocket(port);
            byte[] receiveData = new byte[1024];
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            mSocket.receive(receivePacket);// 在接收到信息之前，一直保持阻塞状态
            address = receivePacket.getAddress();
            System.out.println("客户端说:" + new String(receiveData).trim());
        } catch (SocketException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public int getPort() {
        return port;
    }

    public InetAddress getAddress() {
        return address;
    }
}
