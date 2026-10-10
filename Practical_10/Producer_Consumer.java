package Practical_10;
import java.util.LinkedList;
import java.util.Queue;

public class Producer_Consumer {

    // Shared buffer
    static class SharedBuffer {
        private final Queue<Integer> buffer = new LinkedList<>();
        private final int capacity = 3;

        // Add item to buffer
        public synchronized void produce(int item) throws InterruptedException {
            // Wait if buffer is full
            while (buffer.size() == capacity) {
                wait();
            }
            buffer.add(item);
            System.out.println("Produced: " + item + " | Buffer: " + buffer);
            // Notify consumer
            notifyAll();
        }

        // Remove item from buffer
        public synchronized int consume() throws InterruptedException {
            // Wait if buffer is empty
            while (buffer.isEmpty()) {
                wait();
            }
            int item = buffer.remove();
            System.out.println("Consumed: " + item + " | Buffer: " + buffer);
            // Notify producer
            notifyAll();
            return item;
        }
    }

    public static void main(String[] args) {
        SharedBuffer buffer = new SharedBuffer();
        final int itemCount = 10;
        // Producer thread
        Thread producer = new Thread(() -> {
            try {
                for (int i = 1; i <= itemCount; i++) {
                    buffer.produce(i);
                    Thread.sleep(200);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }, "Producer");

        // Consumer thread
        Thread consumer = new Thread(() -> {
            try {
                for (int i = 1; i <= itemCount; i++) {
                    buffer.consume();
                    Thread.sleep(400);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }, "Consumer");

        // Start both threads
        producer.start();
        consumer.start();

        // Wait for both threads to finish
        try {
            producer.join();
            consumer.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("\nAll items produced and consumed successfully.");
    }
}