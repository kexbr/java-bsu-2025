package com.example.bank.service.strategy;

import com.example.bank.model.Transaction;
import com.example.bank.repository.AccountRepository;

public class DepositStrategy implements TransactionStrategy {
    private final AccountRepository accountRepository;

    public DepositStrategy(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public void execute(Transaction transaction) {
        accountRepository.findById(transaction.getFromAccountId()).ifPresent(account -> {
            if (account.isFrozen()) {
                transaction.setStatus(Transaction.Status.FAILED);
                System.out.println("Ошибка: Счет " + account.getAccountId() + " заморожен.");
                return;
            }
            account.deposit(transaction.getAmount());
            transaction.setStatus(Transaction.Status.COMPLETED);
            System.out.println("Пополнение " + transaction.getAmount() + " на счет " + account.getAccountId() + ". Новый баланс: " + account.getBalance());
        });
    }
}