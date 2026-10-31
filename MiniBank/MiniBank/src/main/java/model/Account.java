package model;

import java.io.Serial;
import java.io.Serializable;
import exception.AccountNotFoundException;
import exception.BankException;
import exception.InsufficientFundsException;
import exception.InvalidAmountException;
import model.annotation.Id;
import model.annotation.MaxLength;
import model.annotation.Positive;

public abstract class Account implements Transactable, InterestBearing,
        Serializable, Comparable<Account> {

    @Serial
    private static final long serialVersionUID = 1L;

    private static int nextAccountNumber = 1;

    @Id
    private final String accountNumber;
    @MaxLength(50)
    private String ownerName;
    @Positive
    protected double balance;

    public Account(String ownerName) {
        this.accountNumber = String.format("AC%04d", nextAccountNumber++);
        this.ownerName = ownerName;
        this.balance = 0;
    }

    public Account(String ownerName, double balance) {
        this.accountNumber = String.format("AC%04d", nextAccountNumber++);
        this.ownerName = ownerName;
        this.balance = balance;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public static void synchronizeAccountNumberSequence(Iterable<? extends Account> accounts) {
        int largestNumber = 0;
        for (Account account : accounts) {
            String number = account.getAccountNumber();
            if (number != null && number.startsWith("AC")) {
                try {
                    largestNumber = Math.max(largestNumber,
                            Integer.parseInt(number.substring(2)));
                } catch (NumberFormatException ignored) {
                    // Ignore account numbers that do not use the generated AC-number format.
                }
            }
        }
        nextAccountNumber = Math.max(nextAccountNumber, largestNumber + 1);
    }

    public String getOwnerName() {
        return ownerName;
    }

    public synchronized double getBalance() {
        return balance;
    }

    @Override
    public synchronized void deposit(long amount) throws InvalidAmountException {
        if (amount <= 0) {
            throw new InvalidAmountException("Deposit amount must be greater than zero.");
        }
        balance += amount;
    }

    @Override
    public synchronized boolean withdraw(long amount)
            throws InsufficientFundsException, InvalidAmountException {
        if (amount <= 0) {
            throw new InvalidAmountException("Withdrawal amount must be greater than zero.");
        }
        if (!canWithdraw(amount)) {
            long shortfall = Math.max(1, amount - Math.max(0, (long) balance));
            throw new InsufficientFundsException(shortfall);
        }
        balance -= amount;
        return true;
    }

    void depositUnsynchronizedForRaceDemo(long amount) throws InvalidAmountException {
        if (amount <= 0) {
            throw new InvalidAmountException("Deposit amount must be greater than zero.");
        }
        double currentBalance = balance;
        Thread.yield();
        balance = currentBalance + amount;
    }

    public void transfer(Account to, long amount) throws BankException {
        boolean withdrawn = false;
        boolean completed = false;
        try {
            if (to == null) {
                throw new AccountNotFoundException("destination");
            }
            if (equals(to)) {
                throw new InvalidAmountException("Source and destination must be different accounts.");
            }
            withdraw(amount);
            withdrawn = true;
            to.deposit(amount);
            completed = true;
        } catch (BankException e) {
            throw e;
        } finally {
            if (withdrawn && !completed) {
                balance += amount;
            }
        }
    }

    public abstract double interestRate();

    public abstract boolean canWithdraw(long amount);

    @Override
    public synchronized String toString() {
        return accountNumber + " | " + ownerName + " | " + balance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o instanceof Account) {
            Account other = (Account) o;
            return this.accountNumber.equals(other.accountNumber);
        }

        return false;
    }

    @Override
    public int hashCode() {
        return accountNumber.hashCode();
    }

    @Override
    public int compareTo(Account other) {
        return accountNumber.compareTo(other.accountNumber);
    }
}
