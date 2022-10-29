package com.example.demo.service;

import com.example.demo.model.Room;

import java.util.List;

public interface IRoomService {

    List<Room> getAllOpenRoom();

    Room createNewRoom(int ownerId, String name, String deccription);

    Room getById(int roomId);

    void joinRoom(int userId, int roomId);
}
