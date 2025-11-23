package com.example.bank.service.factory;

import com.example.bank.model.Transaction;
import com.example.bank.repository.AccountRepository;
import com.example.bank.service.strategy.DepositStrategy;
import com.example.bank.service.strategy.TransactionStrategy;
import com.example.bank.service.strategy.TransferStrategy;
import com.example.bank.service.strategy.WithdrawalStrategy;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TransactionStrategyFactory {
    private final Map<Transaction.Action, TransactionStrategy> strategies = new ConcurrentHashMap<>();

    public TransactionStrategyFactory(AccountRepository accountRepository) {
        strategies.put(Transaction.Action.DEPOSIT, new DepositStrategy(accountRepository));
        strategies.put(Transaction.Action.WITHDRAWAL, new WithdrawalStrategy(accountRepository));
        strategies.put(Transaction.Action.TRANSFER, new TransferStrategy(accountRepository));
    }

    public TransactionStrategy getStrategy(Transaction.Action action) {
        TransactionStrategy strategy = strategies.get(action);
        if (strategy == null) {
            throw new IllegalArgumentException("Неизвестный тип транзакции: " + action);
        }
        return strategy;
    }
}