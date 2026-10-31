package service;

import java.util.ArrayList;
import java.util.List;
import model.BankAccount;

public class BankService {
    private final List<BankAccount> accounts = new ArrayList<>();

    public BankAccount addAccount(String accountNumber, String ownerName, long openingBalance) {
        boolean exists = accounts.stream().anyMatch(
                account -> account.getAccountNumber().equalsIgnoreCase(accountNumber));
        if (exists) throw new IllegalArgumentException("Account number already exists.");
        BankAccount account = new BankAccount(accountNumber, ownerName, openingBalance);
        accounts.add(account);
        return account;
    }

    public int accountCount() { return accounts.size(); }
    public List<BankAccount> listAccounts() { return List.copyOf(accounts); }

    public void validateAction(int action) {
        if (action < 1 || action > 3) {
            throw new IllegalArgumentException("Invalid menu action: " + action);
        }
    }
}
