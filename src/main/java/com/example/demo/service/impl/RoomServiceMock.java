package com.example.demo.service.impl;

import com.example.demo.Demo3Application;
import com.example.demo.model.Room;
import com.example.demo.service.IRoomService;

import java.util.ArrayList;
import java.util.List;

public class RoomServiceMock implements IRoomService {

    private static RoomServiceMock instance = new RoomServiceMock();

    private RoomServiceMock() {

    }

    public static RoomServiceMock getInstance() {
        return instance;
    }

    @Override
    public List<Room> getAllOpenRoom() {
        Room room = createNewRoom(-1, "test", "test");
        List<Room> s = new ArrayList<>();
        s.add(room);
        return s;
    }

    @Override
    public Room createNewRoom(int ownerId, String name, String deccription) {
        Room room = new Room();
        room.setRoomName(name);
        room.setId(Demo3Application.getId());
        room.setOwnerId(ownerId);
        room.setDescription(deccription);
        return room;
    }

    @Override
    public Room getById(int roomId) {
        return null;
    }

    @Override
    public void joinRoom(int userId, int roomId) {

    }
}
