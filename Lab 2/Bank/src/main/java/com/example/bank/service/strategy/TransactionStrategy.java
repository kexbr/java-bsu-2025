package com.example.bank.service.strategy;

import com.example.bank.model.Transaction;

public interface TransactionStrategy {
    void execute(Transaction transaction);
}