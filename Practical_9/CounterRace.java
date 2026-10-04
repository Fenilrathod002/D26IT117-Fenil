class Counter {
    int count = 0;

    void increment() {
        count++;
    }
}
class CounterThread extends Thread {
    Counter counter;
    int times;

    CounterThread(Counter counter, int times) {
        this.counter = counter;
        this.times = times;
    }

    public void run() {
        for (int i = 0; i < times; i++) {
            counter.increment();
        }
    }
}

public class CounterRace {
    public static void main(String[] args) throws InterruptedException {

        Counter counter = new Counter();

        int numberOfThreads = 10;
        int incrementsPerThread = 100000;

        Thread[] threads = new Thread[numberOfThreads];

        for (int i = 0; i < numberOfThreads; i++) {
            threads[i] = new CounterThread(counter, incrementsPerThread);
            threads[i].start();
        }

        for (int i = 0; i < numberOfThreads; i++) {
            threads[i].join();
        }

        int expected = numberOfThreads * incrementsPerThread;

        System.out.println("Expected count: " + expected);
        System.out.println("Actual count:   " + counter.count);
    }
}