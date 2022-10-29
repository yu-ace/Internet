package com.example.demo.service.impl;

import com.example.demo.Demo3Application;
import com.example.demo.model.Record;
import com.example.demo.model.*;
import com.example.demo.server.Session;
import com.example.demo.server.SessionManager;
import com.example.demo.service.IGameService;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameService implements IGameService {

    private static GameService instance = new GameService();
    List<Game> gameList = new ArrayList<>();
    SessionManager sessionManager = SessionManager.getInstance();
    RoomService roomService = RoomService.getInstance();
    TransactionService transactionService = TransactionService.getInstance();

    private GameService() {
    }

    public static GameService getInstance() {
        return instance;
    }

    @Override
    public Game newGame(int roomId) {
        Room room = roomService.getById(roomId);

        Game game = new Game();
        game.setId(Demo3Application.getId());
        game.setRoomId(roomId);
        game.setStatus(0);
        gameList.add(game);

        for (Integer playerId : room.getPlayer()) {
            Session session = sessionManager.getSessionByUserId(playerId);
            session.sendSystemMessage("房主已开启新一轮的游戏，欢迎下注,最新的游戏ID是:" + game.getId());
        }
        return game;
    }

    @Override
    public List<Game> getGames(int roomId) {
        List<Game> gs = new ArrayList<>();
        for (Game game : gameList) {
            if (game.getRoomId() == roomId) {
                gs.add(game);
            }
        }
        return gs;
    }

    @Override
    public Game getLastGame(int roomId) {
        if (gameList.size() == 0) {
            return null;
        }
        return gameList.get(gameList.size() - 1);
    }

    @Override
    public Game joinGame(int gameId, int userId, int amount, int result) {
        Session session = sessionManager.getSessionByUserId(userId);
        User user = session.getUser();
        Game game = this.getById(gameId);
        if (game == null) {
            session.sendSystemMessage("游戏ID不存在，无法下注！");
            return null;
        }
        Room room = roomService.getById(game.getRoomId());
        if (user.getBalance() < amount) {
            session.sendSystemMessage("您的余额不足，无法下注！");
            return null;
        }
        if (room.getOwnerId() == userId) {
            session.sendSystemMessage("您是庄家，无法下注！");
            return null;
        }

        TransactionRecord transactionRecord = new TransactionRecord();
        transactionRecord.setGameId(gameId);
        transactionRecord.setAmount(-1 * amount);
        transactionRecord.setType(0);
        transactionRecord.setGoalId(-1);
        transactionService.newTransactionRecord(transactionRecord);

        user.setBalance(user.getBalance() - amount);

        Record record = new Record();
        record.setUserId(userId);
        record.setAmount(amount);
        record.setResult(result);

        game.getRecordList().add(record);

        for (Integer integer : room.getPlayer()) {
            String rStr = result == -1 ? "小" : "大";
            Session playerSession = sessionManager.getSessionByUserId(integer);
            playerSession.sendSystemMessage(user.getNickname() + " 下注了， 押" + rStr + " 下注金额为:" + amount);
        }
        return game;
    }

    @Override
    public Game endGame(int gameId) {
        Game game = this.getById(gameId);
        Room room = roomService.getById(game.getRoomId());
        Random random = new Random();
        int randomInt = random.nextInt(6);
        int rs = randomInt < 3 ? -1 : 1;
        String rStr = rs == -1 ? "小" : "大";
        game.setStatus(1);

        int sum = 0;
        int count = 0;
        for (Record record : game.getRecordList()) {
            sum = sum + record.getAmount();
            if (record.getResult() == rs) {
                count = count + 1;
            }
        }
        int meanReward = (int) ((sum * 0.9) / count);
        int ownerReward = sum - meanReward * count;
        Session ownerSession = sessionManager.getSessionByUserId(room.getOwnerId());
        String os = String.format("本轮开奖结果为:%s，共%d人参与，中奖的有%d人，总下注金额为%d元，" +
                        "平均奖金为%d元，您获得了%d元佣金",
                rStr, game.getRecordList().size(), count, sum, meanReward, ownerReward);
        ownerSession.sendSystemMessage(os);
        User owner = ownerSession.getUser();
        owner.setBalance(owner.getBalance() + ownerReward);
        TransactionRecord transactionRecord = new TransactionRecord();
        transactionRecord.setGoalId(owner.getId());
        transactionRecord.setUserId(-1);
        transactionRecord.setType(2);
        transactionRecord.setAmount(ownerReward);
        transactionRecord.setGameId(gameId);
        transactionService.newTransactionRecord(transactionRecord);

        for (Record record : game.getRecordList()) {
            int userId = record.getUserId();
            Session session = sessionManager.getSessionByUserId(userId);
            User player = session.getUser();
            if (record.getResult() == rs) {
                TransactionRecord transactionRecord2 = new TransactionRecord();
                transactionRecord2.setGoalId(record.getUserId());
                transactionRecord2.setUserId(-1);
                transactionRecord2.setType(1);
                transactionRecord2.setAmount(meanReward);
                transactionRecord2.setGameId(gameId);
                transactionService.newTransactionRecord(transactionRecord);
                player.setBalance(player.getBalance() + meanReward);
                session.sendSystemMessage("本轮开奖结果为:" + rStr + " 您赢了:" + meanReward);
            } else {
                session.sendSystemMessage("本轮开奖结果为:" + rStr + " 您输了：" + record.getAmount());
            }
        }
        return game;
    }

    @Override
    public Game getById(int gameId) {
        for (Game game : gameList) {
            if (game.getId() == gameId) {
                return game;
            }
        }
        return null;
    }

}
