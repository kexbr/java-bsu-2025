package com.example.bank.service.observer;

import com.example.bank.model.Transaction;

public interface TransactionObserver {
    void update(Transaction transaction);
}