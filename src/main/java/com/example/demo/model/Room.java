package com.example.demo.model;

import java.util.List;

public class Room {

    int id;
    int ownerId;
    String roomName;
    String description;
    List<Integer> player;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }

    public List<Integer> getPlayer() {
        return player;
    }

    public void setPlayer(List<Integer> player) {
        this.player = player;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
