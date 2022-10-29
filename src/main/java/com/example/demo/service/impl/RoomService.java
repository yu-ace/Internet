package com.example.demo.service.impl;

import com.example.demo.Demo3Application;
import com.example.demo.model.Room;
import com.example.demo.service.IRoomService;

import java.util.ArrayList;
import java.util.List;

public class RoomService implements IRoomService {

    private static RoomService instance = new RoomService();
    List<Room> roomList = new ArrayList<>();

    private RoomService() {
    }

    public static RoomService getInstance() {
        return instance;
    }

    @Override
    public List<Room> getAllOpenRoom() {
        return roomList;
    }

    @Override
    public Room createNewRoom(int ownerId, String name, String deccription) {
        Room room = new Room();
        room.setRoomName(name);
        room.setId(Demo3Application.getId());
        room.setOwnerId(ownerId);
        room.setDescription(deccription);
        roomList.add(room);
        return room;
    }

    @Override
    public Room getById(int roomId) {
        for (Room room : roomList) {
            if (room.getId() == roomId) {
                return room;
            }
        }
        return null;
    }

    @Override
    public void joinRoom(int userId, int roomId) {
        Room room = getById(roomId);
        room.getPlayer().add(userId);
    }
}