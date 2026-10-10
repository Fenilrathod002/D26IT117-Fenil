package Practical_10;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Thread_Pool_Runner {

    public static void main(String[] args) {
        // Create a fixed thread pool with 3 threads
        ExecutorService executor = Executors.newFixedThreadPool(3);


        // Submit 10 tasks
        for (int i = 1; i <= 10; i++) {
            final int taskId = i;
            executor.submit(() -> {
                System.out.println("Task " + taskId + " is running on " +Thread.currentThread().getName());

                try {
                    // Sleep briefly to simulate some work
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    System.out.println("Task " + taskId + " was interrupted.");
                }

                System.out.println("Task " + taskId + " completed by " +Thread.currentThread().getName()
                );
            });
        }

        // No more tasks can be submitted
        executor.shutdown();

        try {
            // Wait until all tasks are completed
            if (executor.awaitTermination(30, TimeUnit.SECONDS)) {
                System.out.println("\nAll tasks completed.");
            } else {
                System.out.println("\nTimeout occurred. Some tasks may still be running.");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Main thread was interrupted.");
        }
    }
}