package com.example.bank.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class Transaction {

    public enum Action {
        DEPOSIT, WITHDRAWAL, FREEZE, TRANSFER
    }

    private final UUID transactionId;
    private final LocalDateTime timestamp;
    private final Action action;
    private final String fromAccountId;
    private final String toAccountId;
    private final BigDecimal amount;
    private Status status;

    public enum Status {
        PENDING, COMPLETED, FAILED
    }

    public Transaction(Action action, String fromAccountId, String toAccountId, BigDecimal amount) {
        this.transactionId = UUID.randomUUID();
        this.timestamp = LocalDateTime.now();
        this.action = action;
        this.fromAccountId = fromAccountId;
        this.toAccountId = toAccountId;
        this.amount = amount;
        this.status = Status.PENDING;
    }

    public UUID getTransactionId() { return transactionId; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public Action getAction() { return action; }
    public String getFromAccountId() { return fromAccountId; }
    public String getToAccountId() { return toAccountId; }
    public BigDecimal getAmount() { return amount; }
    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }

    @Override
    public String toString() {
        return "Transaction{" +
                "transactionId=" + transactionId +
                ", action=" + action +
                ", fromAccountId='" + fromAccountId + '\'' +
                ", toAccountId='" + toAccountId + '\'' +
                ", amount=" + amount +
                ", status=" + status +
                '}';
    }
}