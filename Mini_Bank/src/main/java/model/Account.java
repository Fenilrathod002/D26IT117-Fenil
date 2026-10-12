package model;

import exception.*;
import java.io.*;
import java.util.Objects;
import model.annotation.Id;
import model.annotation.Positive;

public abstract class Account implements Transactable, Serializable {

    private static final long serialVersionUID = 1L;
    private static long counter = 1;
    @Id
    private final String accountNumber;
    private final String ownerName;
    @Positive
    private long balance;
    private boolean active = true;
    private transient Object lock = new Object();

    protected Account(String ownerName, long openingBalance) {
        if (ownerName == null || ownerName.isBlank() || openingBalance < 0) {
            throw new IllegalArgumentException("Owner and non-negative opening balance are required");
        
        }this.accountNumber = nextNumber();
        this.ownerName = ownerName;
        this.balance = openingBalance;
    }

    private static synchronized String nextNumber() {
        return String.format("AC%04d", counter++);
    }

    public final String getAccountNumber() {
        return accountNumber;
    }

    public final String getOwnerName() {
        return ownerName;
    }

    public final long getBalance() {
        synchronized (lock) {
            return balance;
        }
    }

    public final boolean isActive() {
        return active;
    }

    protected final Object accountLock() {
        return lock;
    }

    protected final long balanceUnsafe() {
        return balance;
    }

    @Override
    public final void deposit(long amount) throws InvalidAmountException {
        synchronized (lock) {
            validateAmount(amount);
            balance = Math.addExact(balance, amount);
        }
    }

    @Override
    public final boolean withdraw(long amount) throws InsufficientFundsException, InvalidAmountException {
        synchronized (lock) {
            validateAmount(amount);
            if (!canWithdraw(amount)) {
                throw new InsufficientFundsException("Withdrawal denied by account policy", Math.max(0, amount - Math.max(0, balance)));
            
            }balance -= amount;
            return true;
        }
    }

    public final void transfer(Account to, long amount) throws BankException {
        if (to == null) {
            throw new AccountNotFoundException("Destination account is required");
        
        }if (to == this) {
            throw new BankException("Cannot transfer to the same account");
        
        }Account first = this.accountNumber.compareTo(to.accountNumber) < 0 ? this : to;
        Account second = first == this ? to : this;
        synchronized (first.lock) {
            synchronized (second.lock) {
                try {
                    validateAmount(amount);
                    if (!canWithdraw(amount)) {
                        throw new InsufficientFundsException("Transfer denied by account policy", Math.max(0, amount - Math.max(0, balance)));
                    
                    }this.balance -= amount;
                    try {
                        to.balance = Math.addExact(to.balance, amount);
                    } catch (ArithmeticException e) {
                        this.balance += amount;
                        throw new BankException("Transfer rolled back", e);
                    }
                } finally {
                    /* locks release automatically */ }
            }
        }
    }

    private static void validateAmount(long amount) throws InvalidAmountException {
        if (amount <= 0) {
            throw new InvalidAmountException("Amount must be positive");
    
        }}

    public abstract double interestRate();

    public abstract boolean canWithdraw(long amount);

    public final double monthlyInterest() {
        return getBalance() * interestRate() / 1200.0;
    }

    @Override
    public final boolean equals(Object other) {
        return other instanceof Account a && accountNumber.equals(a.accountNumber);
    }

    @Override
    public final int hashCode() {
        return Objects.hash(accountNumber);
    }

    @Override
    public String toString() {
        return "%s{number=%s, owner=%s, balance=%d, active=%s}".formatted(getClass().getSimpleName(), accountNumber, ownerName, getBalance(), active);
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        lock = new Object();
    }
}
