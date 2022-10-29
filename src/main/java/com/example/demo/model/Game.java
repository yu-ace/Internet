package com.example.demo.model;

import java.util.List;

public class Game {
    int id;
    int roomId;
    List<Integer> users;
    List<Record> recordList;
    /**
     * 0 等待下注
     * 1 等待开奖
     * 2 游戏结束
     */
    int status;
}
