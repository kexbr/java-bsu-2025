package com.example.bank.service.strategy;

import com.example.bank.model.Account;
import com.example.bank.model.Transaction;
import com.example.bank.repository.AccountRepository;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

public class TransferStrategy implements TransactionStrategy {
    private final AccountRepository accountRepository;

    public TransferStrategy(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public void execute(Transaction transaction) {
        Account fromAccount = accountRepository.findById(transaction.getFromAccountId()).orElse(null);
        Account toAccount = accountRepository.findById(transaction.getToAccountId()).orElse(null);

        if (fromAccount == null || toAccount == null) {
            transaction.setStatus(Transaction.Status.FAILED);
            System.out.println("Ошибка: Один из счетов не найден.");
            return;
        }

        Object lock1 = transaction.getFromAccountId().compareTo(transaction.getToAccountId()) < 0 ? fromAccount : toAccount;
        Object lock2 = transaction.getFromAccountId().compareTo(transaction.getToAccountId()) < 0 ? toAccount : fromAccount;

        synchronized (lock1) {
            synchronized (lock2) {
                if (fromAccount.isFrozen() || toAccount.isFrozen()) {
                    transaction.setStatus(Transaction.Status.FAILED);
                    System.out.println("Ошибка: Один из счетов заморожен.");
                    return;
                }
                if (fromAccount.withdraw(transaction.getAmount())) {
                    toAccount.deposit(transaction.getAmount());
                    transaction.setStatus(Transaction.Status.COMPLETED);
                    System.out.println("Перевод " + transaction.getAmount() + " со счета " + fromAccount.getAccountId() + " на счет " + toAccount.getAccountId());
                } else {
                    transaction.setStatus(Transaction.Status.FAILED);
                    System.out.println("Ошибка: Недостаточно средств на счете " + fromAccount.getAccountId());
                }
            }
        }
    }
}