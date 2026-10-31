package service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Collection;
import model.Account;

/** Runs bank transaction tasks on a fixed pool of four worker threads. */
public final class TransactionProcessor {
    private final ExecutorService executor = Executors.newFixedThreadPool(4);
    private final ConcurrentHashMap<String, Account> sharedAccounts = new ConcurrentHashMap<>();

    public TransactionProcessor() { }

    public TransactionProcessor(Collection<? extends Account> accounts) {
        for (Account account : accounts) {
            registerAccount(account);
        }
    }

    public void registerAccount(Account account) {
        if (account == null) {
            throw new IllegalArgumentException("Account cannot be null.");
        }
        sharedAccounts.put(account.getAccountNumber(), account);
    }

    public Account getAccount(String accountNumber) {
        return sharedAccounts.get(accountNumber);
    }

    public int getSharedAccountCount() {
        return sharedAccounts.size();
    }

    public void submit(Runnable task) {
        executor.execute(task);
    }

    public void stop() throws InterruptedException {
        executor.shutdown();
        if (!executor.awaitTermination(30, TimeUnit.SECONDS)) {
            executor.shutdownNow();
            if (!executor.awaitTermination(30, TimeUnit.SECONDS)) {
                throw new IllegalStateException("Transaction workers did not stop.");
            }
        }
    }

    public void stopNow() {
        executor.shutdownNow();
    }
}
