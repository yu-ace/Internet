package com.example.demo.service;

import com.example.demo.model.TransactionRecord;

import java.util.List;

public interface ITransactionService {

    TransactionRecord newTransactionRecord(TransactionRecord record);

    List<TransactionRecord> getByUserId(int userId);

    List<TransactionRecord> getByGameId(int gameId);

}
