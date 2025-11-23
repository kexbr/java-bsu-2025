package com.example.bank;

import com.example.bank.model.Account;
import com.example.bank.model.Transaction;
import com.example.bank.repository.AccountRepository;
import com.example.bank.repository.InMemoryAccountRepository;
import com.example.bank.service.TransactionProcessor;
import com.example.bank.service.command.TransactionCommand;
import com.example.bank.service.factory.TransactionStrategyFactory;
import com.example.bank.service.observer.LoggingObserver;

import java.math.BigDecimal;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        // Инициализация системы.
        // Создаем базу данных в памяти для хранения счетов.
        AccountRepository accountRepository = new InMemoryAccountRepository();
        // Создаем фабрику, которая будет выдавать нужную стратегию (логику) для каждого типа транзакции.
        TransactionStrategyFactory strategyFactory = new TransactionStrategyFactory(accountRepository);
        // Получаем единственный экземпляр асинхронного обработчика транзакций (Singleton).
        TransactionProcessor processor = TransactionProcessor.getInstance();
        // Добавляем наблюдателя, который будет логировать информацию о завершении транзакций (Observer).
        processor.addObserver(new LoggingObserver());

        // Создание исходных счетов.
        Account account1 = new Account("ACC001", new BigDecimal("1000.00"));
        Account account2 = new Account("ACC002", new BigDecimal("500.00"));
        accountRepository.save(account1);
        accountRepository.save(account2);

        System.out.println("Начальное состояние:");
        System.out.println(accountRepository.findById("ACC001"));
        System.out.println(accountRepository.findById("ACC002"));
        System.out.println("------------------------------------");

        // Здесь мы создаем различные транзакции для демонстрации.
        // Все команды ниже отправляются на выполнение асинхронно, то есть не ожидая завершения предыдущей.
        // Они будут обработаны параллельно в пуле потоков.
        Transaction deposit = new Transaction(Transaction.Action.DEPOSIT, "ACC001", null, new BigDecimal("200.00"));
        Transaction withdrawal = new Transaction(Transaction.Action.WITHDRAWAL, "ACC002", null, new BigDecimal("100.00"));
        Transaction transfer = new Transaction(Transaction.Action.TRANSFER, "ACC001", "ACC002", new BigDecimal("300.00"));
        Transaction failedWithdrawal = new Transaction(Transaction.Action.WITHDRAWAL, "ACC002", null, new BigDecimal("9999.00"));

        TransactionCommand depositCmd = new TransactionCommand(deposit, strategyFactory);
        TransactionCommand withdrawalCmd = new TransactionCommand(withdrawal, strategyFactory);
        TransactionCommand transferCmd = new TransactionCommand(transfer, strategyFactory);
        TransactionCommand failedWithdrawalCmd = new TransactionCommand(failedWithdrawal, strategyFactory);

        processor.process(depositCmd);
        processor.process(withdrawalCmd);
        processor.process(transferCmd);
        processor.process(failedWithdrawalCmd);

        System.out.println("Транзакции отправлены на обработку...");
        TimeUnit.SECONDS.sleep(3);
        processor.shutdown();

        System.out.println("------------------------------------");
        System.out.println("Конечное состояние:");
        System.out.println(accountRepository.findById("ACC001"));
        System.out.println(accountRepository.findById("ACC002"));
    }
}