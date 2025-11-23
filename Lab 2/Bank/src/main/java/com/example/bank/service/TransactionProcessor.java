package com.example.bank.service;

import com.example.bank.service.command.Command;
import com.example.bank.service.observer.TransactionObserver;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TransactionProcessor {
    private static volatile TransactionProcessor instance;
    private final ExecutorService executorService;
    private final List<TransactionObserver> observers = new ArrayList<>();

    private TransactionProcessor() {
        this.executorService = Executors.newFixedThreadPool(10);
    }

    public static TransactionProcessor getInstance() {
        if (instance == null) {
            synchronized (TransactionProcessor.class) {
                if (instance == null) {
                    instance = new TransactionProcessor();
                }
            }
        }
        return instance;
    }

    public void addObserver(TransactionObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(TransactionObserver observer) {
        observers.remove(observer);
    }

    public void process(Command command) {
        executorService.submit(() -> {
            command.execute();
        });
    }

    public void shutdown() {
        executorService.shutdown();
    }
}