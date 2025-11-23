package com.example.bank.model;

import java.math.BigDecimal;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Account {
    private final String accountId;
    private BigDecimal balance;
    private boolean isFrozen;
    private final Lock lock = new ReentrantLock();
    public Account(String accountId, BigDecimal balance) {
        this.accountId = accountId;
        this.balance = balance;
        this.isFrozen = false;
    }
    public String getAccountId() {
        return accountId;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public boolean isFrozen() {
        return isFrozen;
    }

    public Lock getLock() {
        return lock;
    }
    public void setFrozen(boolean frozen) {
        isFrozen = frozen;
    }
    public void deposit(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) > 0) {
            this.balance = this.balance.add(amount);
        }
    }
    public boolean withdraw(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) > 0 && this.balance.compareTo(amount) >= 0) {
            this.balance = this.balance.subtract(amount);
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return "Account{" +
                "accountId='" + accountId + '\'' +
                ", balance=" + balance +
                ", isFrozen=" + isFrozen +
                '}';
    }
}