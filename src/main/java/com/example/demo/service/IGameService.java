package com.example.demo.service;

import com.example.demo.model.Game;

import java.util.List;

public interface IGameService {

    Game newGame(int roomId, int userId);

    List<Game> getGames(int roomId);

    Game getLastGame(int roomId);

    Game joinGame(int gameId, int userId, int amount, int result);

    Game endGame(int gameId);
}
