package service;

import java.util.Scanner;

public final class BankSession implements AutoCloseable {
    private final Scanner scanner = new Scanner(System.in);

    public Scanner scanner() {
        return scanner;
    }

    @Override
    public void close() {
        scanner.close();
    }
}
