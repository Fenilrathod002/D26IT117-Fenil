package service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import model.Account;

/** Stores accounts by account number and tracks transactions for this session. */
public final class AccountStore {
    private final HashMap<String, Account> accounts = new HashMap<>();
    private final ArrayList<String> transactionHistory = new ArrayList<>();

    public void add(Account account) {
        if (account == null) {
            throw new IllegalArgumentException("Account cannot be null.");
        }
        accounts.put(account.getAccountNumber(), account);
    }

    public Account get(String accountNumber) {
        return accounts.get(accountNumber);
    }

    public ArrayList<Account> getAll() {
        return new ArrayList<>(accounts.values());
    }

    /** Returns accounts from highest balance to lowest; account number breaks ties. */
    public ArrayList<Account> sortedByBalance() {
        ArrayList<Account> sortedAccounts = getAll();
        sortedAccounts.sort(Comparator.comparingDouble(Account::getBalance)
                .reversed().thenComparing(Account::getAccountNumber));
        return sortedAccounts;
    }

    public void recordTransaction(String transaction) {
        transactionHistory.add(transaction);
    }

    public ArrayList<String> getTransactionHistory() {
        return new ArrayList<>(transactionHistory);
    }
}
