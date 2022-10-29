package com.example.demo.server;

import com.alibaba.fastjson2.JSON;
import com.example.demo.Demo3Application;
import com.example.demo.model.Message;
import com.example.demo.model.Room;
import com.example.demo.model.TransactionRecord;
import com.example.demo.model.User;
import com.example.demo.service.IGameService;
import com.example.demo.service.IRoomService;
import com.example.demo.service.ITransactionService;
import com.example.demo.service.impl.GameService;
import com.example.demo.service.impl.RoomService;
import com.example.demo.service.impl.TransactionService;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.List;

public class Session implements Closeable, Runnable {
    User user;
    Socket socket;
    Thread thread;

    boolean isRunning = true;

    IRoomService roomService = RoomService.getInstance();
    ITransactionService transactionService = TransactionService.getInstance();
    IGameService gameService = GameService.getInstance();

    public Session() {
    }

    public Session(Socket socket) {
        this.socket = socket;
        thread = new Thread(this);
        thread.start();
        Message message2 = new Message(-1, "MESSAGE", "欢迎使用QQ游戏厅");
        try {
            socket.getOutputStream().write(JSON.toJSONString(message2).getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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
                // 处理系统信息
                handleCommand(message1);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private void handleCommand(Message message1) {
        if (message1.getCommand().equals("LIST_ROOM")) {
            listRoom();
        } else if (message1.getCommand().equals("LOGIN")) {
            login(message1);
        } else if (message1.getCommand().equals("LOGOUT")) {
            logout(message1);
        } else if (message1.getCommand().equals("NEW_ROOM")) {
            roomService.createNewRoom
                    (message1.getUserId(), message1.getNickName(), message1.getContent());
            sendSystemMessage("创建成功");
        } else if (message1.getCommand().equals("JOIN_ROOM")) {
            roomService.joinRoom(message1.getUserId(), message1.getRoomId());
            sendSystemMessage("加入成功");
        } else if (message1.getCommand().equals("GET_BALANCE")) {
            sendSystemMessage("您的余额为:" + user.getBalance());
        } else if (message1.getCommand().equals("GET_LIST")) {
            getTransaction(message1);
        } else if (message1.getCommand().equals("NEW_GAME")) {
            Room room = roomService.getById(message1.getRoomId());
            if (room.getOwnerId() != user.getId()) {
                sendSystemMessage("您不是房主，无法创建游戏!");
            } else {
                gameService.newGame(message1.getRoomId());
            }
        } else if (message1.getCommand().equals("JOIN_GAME")) {
            gameService.joinGame(message1.getGameId(), user.getId(),
                    message1.getAmount(), message1.getResult());
        } else if (message1.getCommand().equals("END_GAME")) {
            gameService.endGame(message1.getGameId());
        }
    }


    private void getTransaction(Message message1) {
        List<TransactionRecord> byUserId = transactionService.getByUserId(message1.getUserId());
        String ls = "";
        for (TransactionRecord t : byUserId) {
            String a = "下注";
            switch (t.getType()) {
                case 1:
                    a = "赢钱";
                    break;
                case 2:
                    a = "佣金收入";
                    break;
            }
            String line = String.format("%s %d", a, t.getAmount());
            ls = ls + line + "\n";
        }
        Message message = new Message();
        message.setContent(ls);
        message.setCommand("MESSAGE");
        sendCmd2Client(message);
    }

    private void logout(Message message1) {
        isRunning = false;
        System.out.printf("用户%s退出\n", message1.getNickName());
        sendSystemMessage("BYE");
        try {
            this.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void login(Message message1) {
        User user = new User();
        user.setId(Demo3Application.getId());
        user.setBalance(100);
        user.setNickname(message1.getNickName());
        this.setUser(user);

        Message message = new Message();
        message.setUserId(user.getId());
        message.setCommand("LOGIN_OK");
        sendCmd2Client(message);
    }

    private void listRoom() {
        // 发送ROOM列表
        // toddo
        List<Room> allOpenRoom = roomService.getAllOpenRoom();
        String ls = "";
        for (Room room : allOpenRoom) {
            String line = String.format("%d %s %s", room.getId(), room.getRoomName(), room.getDescription());
            ls = ls + line + "\n";
        }
        Message message = new Message();
        message.setContent(ls);
        message.setCommand("MESSAGE");
        sendCmd2Client(message);
    }

    public void sendSystemMessage(String message) {
        Message message2 = new Message(-1, "MESSAGE", message);
        try {
            socket.getOutputStream().write(JSON.toJSONString(message2).getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void sendCmd2Client(Message message) {
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
        message = message.trim();
        System.out.println("接收到消息:" + message);
        Message message1 = JSON.parseObject(message, Message.class);
        return message1;
    }
}
