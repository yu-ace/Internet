package com.example.demo.service.impl;

import com.example.demo.Demo3Application;
import com.example.demo.model.TransactionRecord;
import com.example.demo.service.ITransactionService;

import java.util.ArrayList;
import java.util.List;

public class TransactionService implements ITransactionService {

    private static TransactionService instance = new TransactionService();
    List<TransactionRecord> transactionRecordList = new ArrayList<>();

    private TransactionService() {
    }

    public static TransactionService getInstance() {
        return instance;
    }

    @Override
    public TransactionRecord newTransactionRecord(TransactionRecord record) {
        record.setId(Demo3Application.getId());
        transactionRecordList.add(record);
        return record;
    }

    @Override
    public List<TransactionRecord> getByUserId(int userId) {
        List<TransactionRecord> transactionRecords = new ArrayList<>();
        for (TransactionRecord transactionRecord : transactionRecordList) {
            if (transactionRecord.getUserId() == userId) {
                transactionRecords.add(transactionRecord);
            }
        }
        return transactionRecords;
    }

    @Override
    public List<TransactionRecord> getByGameId(int gameId) {
        List<TransactionRecord> trs = new ArrayList<>();
        for (TransactionRecord transactionRecord : transactionRecordList) {
            if (transactionRecord.getGameId() == gameId) {
                trs.add(transactionRecord);
            }
        }
        return trs;
    }
}
