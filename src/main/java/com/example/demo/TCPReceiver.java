package com.example.demo;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TCPReceiver implements Runnable {

    Socket socket;

    public TCPReceiver(Socket socket) {
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
                System.out.println(message.trim());
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}
