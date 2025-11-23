package com.example.bank.service.observer;

import com.example.bank.model.Transaction;

public class LoggingObserver implements TransactionObserver {
    @Override
    public void update(Transaction transaction) {
        System.out.println("[LOG] Наблюдатель: Транзакция " + transaction.getTransactionId() + " завершена со статусом " + transaction.getStatus());
    }
}