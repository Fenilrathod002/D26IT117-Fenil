package service;

import java.util.concurrent.*;

public final class TransactionProcessor {

    private final ExecutorService pool = Executors.newFixedThreadPool(4);

    public void submit(Runnable task) {
        pool.execute(task);
    }

    public void stop() {
        pool.shutdown();
        try {
            if (!pool.awaitTermination(10, TimeUnit.SECONDS)) {
                pool.shutdownNow();
        
            }} catch (InterruptedException e) {
            pool.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}
