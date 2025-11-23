package com.example.bank.service.command;

import com.example.bank.model.Transaction;
import com.example.bank.service.factory.TransactionStrategyFactory;
import com.example.bank.service.strategy.TransactionStrategy;

public class TransactionCommand implements Command {
    private final Transaction transaction;
    private final TransactionStrategyFactory strategyFactory;

    public TransactionCommand(Transaction transaction, TransactionStrategyFactory strategyFactory) {
        this.transaction = transaction;
        this.strategyFactory = strategyFactory;
    }

    @Override
    public void execute() {
        try {
            TransactionStrategy strategy = strategyFactory.getStrategy(transaction.getAction());
            strategy.execute(transaction);
        } catch (Exception e) {
            transaction.setStatus(Transaction.Status.FAILED);
            System.err.println("ПРОИЗОШЛА КРИТИЧЕСКАЯ ОШИБКА при обработке транзакции " + transaction.getTransactionId() + ": " + e.getMessage());
        }
    }
}