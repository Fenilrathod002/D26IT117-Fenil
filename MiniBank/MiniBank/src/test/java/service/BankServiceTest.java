package service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import exception.BankException;
import exception.InsufficientFundsException;
import model.Account;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BankServiceTest {
    private BankService bankService;

    @BeforeEach
    void createService() {
        bankService = new BankService(new Repository<>());
    }

    @Test
    void depositIncreasesAccountBalance() throws BankException {
        Account account = bankService.openAccount(
                "Test Customer", "test@example.com", "9876543210", 1, 5000);

        bankService.deposit(account.getAccountNumber(), 1200);

        assertEquals(6200.0, account.getBalance(), 0.0);
    }

    @Test
    void withdrawalBeyondSavingsLimitThrowsException() throws BankException {
        Account account = bankService.openAccount(
                "Test Customer", "test@example.com", "9876543210", 1, 1500);

        assertThrows(InsufficientFundsException.class,
                () -> bankService.withdraw(account.getAccountNumber(), 600));
    }

    @Test
    void concurrentDepositsKeepTheExpectedBalance() throws Exception {
        Account account = bankService.openAccount(
                "Test Customer", "test@example.com", "9876543210", 1, 0);
        int workerCount = 8;
        int depositsPerWorker = 1000;
        ExecutorService workers = Executors.newFixedThreadPool(workerCount);
        List<Future<?>> jobs = new ArrayList<>();

        try {
            for (int worker = 0; worker < workerCount; worker++) {
                jobs.add(workers.submit(() -> {
                    for (int deposit = 0; deposit < depositsPerWorker; deposit++) {
                        try {
                            bankService.deposit(account.getAccountNumber(), 1);
                        } catch (BankException e) {
                            throw new AssertionError("Concurrent deposit failed.", e);
                        }
                    }
                }));
            }
            for (Future<?> job : jobs) {
                job.get();
            }
        } finally {
            workers.shutdownNow();
        }

        assertEquals((double) workerCount * depositsPerWorker, account.getBalance(), 0.0);
    }
}
