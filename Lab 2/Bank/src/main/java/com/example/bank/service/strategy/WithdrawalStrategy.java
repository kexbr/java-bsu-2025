package com.example.bank.service.strategy;

import com.example.bank.model.Transaction;
import com.example.bank.repository.AccountRepository;

public class WithdrawalStrategy implements TransactionStrategy {
    private final AccountRepository accountRepository;

    public WithdrawalStrategy(AccountRepository accountRepository) {
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
            if (account.withdraw(transaction.getAmount())) {
                transaction.setStatus(Transaction.Status.COMPLETED);
                System.out.println("Снятие " + transaction.getAmount() + " со счета " + account.getAccountId() + ". Новый баланс: " + account.getBalance());
            } else {
                transaction.setStatus(Transaction.Status.FAILED);
                System.out.println("Ошибка: Недостаточно средств на счете " + account.getAccountId());
            }
        });
    }
}