package com.example.demo.model;

import java.util.ArrayList;
import java.util.List;

public class Game {
    int id;
    int roomId;
    List<Integer> users = new ArrayList<>();
    List<Record> recordList = new ArrayList<>();
    /**
     * 0 等待下注
     * 1 已开奖
     */
    int status;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public List<Integer> getUsers() {
        return users;
    }

    public void setUsers(List<Integer> users) {
        this.users = users;
    }

    public List<Record> getRecordList() {
        return recordList;
    }

    public void setRecordList(List<Record> recordList) {
        this.recordList = recordList;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
