package service;

public final class TransactionBuffer {
    private final Runnable[] items;
    private int head;
    private int size;

    public TransactionBuffer(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("Buffer capacity must be positive.");
        }
        items = new Runnable[capacity];
    }

    public synchronized void put(Runnable transaction) throws InterruptedException {
        if (transaction == null) {
            throw new IllegalArgumentException("Transaction cannot be null.");
        }
        while (size == items.length) {
            wait();
        }
        int tail = (head + size) % items.length;
        items[tail] = transaction;
        size++;
        notifyAll();
    }

    public synchronized Runnable take() throws InterruptedException {
        while (size == 0) {
            wait();
        }
        Runnable transaction = items[head];
        items[head] = null;
        head = (head + 1) % items.length;
        size--;
        notifyAll();
        return transaction;
    }
}
