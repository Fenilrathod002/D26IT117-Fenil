package model;

public class BankAccount {
    private final String accountNumber;
    private final String ownerName;
    private long balance;

    public BankAccount(String accountNumber, String ownerName, long openingBalance) {
        if (accountNumber == null || accountNumber.isBlank()) {
            throw new IllegalArgumentException("Account number cannot be blank.");
        }
        if (ownerName == null || ownerName.isBlank()) {
            throw new IllegalArgumentException("Owner name cannot be blank.");
        }
        if (openingBalance < 0) {
            throw new IllegalArgumentException("Opening balance cannot be negative.");
        }
        this.accountNumber = accountNumber;
        this.ownerName = ownerName;
        this.balance = openingBalance;
    }

    public String getAccountNumber() { return accountNumber; }
    public String getOwnerName() { return ownerName; }
    public long getBalance() { return balance; }

    public void deposit(long amount) {
        if (amount <= 0) throw new IllegalArgumentException("Deposit must be positive.");
        balance = Math.addExact(balance, amount);
    }

    @Override
    public String toString() {
        return accountNumber + " | " + ownerName + " | balance=" + balance;
    }
}
