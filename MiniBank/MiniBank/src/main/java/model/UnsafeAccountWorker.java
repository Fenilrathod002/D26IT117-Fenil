package model;

import exception.InvalidAmountException;

/** Demonstrates lost updates using the deliberately unsynchronized demo method. */
public final class UnsafeAccountWorker implements Runnable {
    private final Account account;
    private final int times;
    private final long amount;

    public UnsafeAccountWorker(Account account, int times, long amount) {
        this.account = account;
        this.times = times;
        this.amount = amount;
    }

    @Override
    public void run() {
        String threadName = Thread.currentThread().getName();
        System.out.println(threadName + " started.");
        for (int i = 0; i < times; i++) {
            try {
                account.depositUnsynchronizedForRaceDemo(amount);
            } catch (InvalidAmountException e) {
                System.out.println(threadName + " stopped: " + e.getMessage());
                return;
            }
        }
        System.out.println(threadName + " finished.");
    }
}
