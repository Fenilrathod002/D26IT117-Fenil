package service;

import model.Command;

public final class TransactionBuffer {

    private final Command[] buffer;
    private int head, tail, count;
    private boolean closed;

    public TransactionBuffer(int capacity) {
        buffer = new Command[capacity];
    }

    public synchronized void put(Command c) throws InterruptedException {
        while (count == buffer.length && !closed) {
            wait();
        
        }if (closed) {
            throw new IllegalStateException("Buffer closed");
        
        }buffer[tail] = c;
        tail = (tail + 1) % buffer.length;
        count++;
        notifyAll();
    }

    public synchronized Command take() throws InterruptedException {
        while (count == 0 && !closed) {
            wait();
        
        }if (count == 0) {
            return null;
        
        }Command c = buffer[head];
        head = (head + 1) % buffer.length;
        count--;
        notifyAll();
        return c;
    }

    public synchronized void close() {
        closed = true;
        notifyAll();
    }
}
