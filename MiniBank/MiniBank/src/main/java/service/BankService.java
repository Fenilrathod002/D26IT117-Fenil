package service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Locale;
import exception.AccountNotFoundException;
import exception.BankException;
import exception.InvalidAmountException;
import model.Account;
import model.CurrentAccount;
import model.FixedDepositAccount;
import model.SavingsAccount;
import util.StatementFormatter;
import util.Validator;

/** Banking operations between the console menu and the account repository. */
public final class BankService {
    private final Repository<Account> accounts;

    public BankService(Repository<Account> accounts) {
        if (accounts == null) {
            throw new IllegalArgumentException("Account repository cannot be null.");
        }
        this.accounts = accounts;
    }

    public Account openAccount(String ownerName, String email, String mobile,
                               int accountType, long openingBalance) throws BankException {
        if (ownerName == null || ownerName.isBlank()) {
            throw new BankException("Owner name cannot be empty.");
        }
        if (!Validator.isValidEmail(email)) {
            throw new BankException("Invalid email address.");
        }
        if (!Validator.isValidMobile(mobile)) {
            throw new BankException("Invalid mobile number. Use a 10-digit number starting with 6-9.");
        }
        if (openingBalance < 0) {
            throw new InvalidAmountException("Opening balance cannot be negative.");
        }

        Account account = switch (accountType) {
            case 1 -> new SavingsAccount(ownerName.trim(), openingBalance, 1000);
            case 2 -> new CurrentAccount(ownerName.trim(), openingBalance, 5000);
            case 3 -> new FixedDepositAccount(ownerName.trim(), openingBalance);
            default -> throw new BankException("Invalid account type.");
        };
        accounts.save(account);
        return account;
    }

    public Account getAccount(String accountNumber) throws AccountNotFoundException {
        Account account = accounts.findById(normalizeAccountNumber(accountNumber));
        if (account == null) {
            throw new AccountNotFoundException(accountNumber);
        }
        return account;
    }

    public Account deposit(String accountNumber, long amount) throws BankException {
        Account account = getAccount(accountNumber);
        account.deposit(amount);
        return account;
    }

    public Account withdraw(String accountNumber, long amount) throws BankException {
        Account account = getAccount(accountNumber);
        account.withdraw(amount);
        return account;
    }

    public void transfer(String fromAccountNumber, String toAccountNumber, long amount)
            throws BankException {
        Account from = getAccount(fromAccountNumber);
        Account to = getAccount(toAccountNumber);
        Account first = from.compareTo(to) < 0 ? from : to;
        Account second = first == from ? to : from;
        synchronized (first) {
            synchronized (second) {
                from.transfer(to, amount);
            }
        }
    }

    public String statement(String accountNumber) throws AccountNotFoundException {
        return StatementFormatter.buildStatement(getAccount(accountNumber));
    }

    public ArrayList<Account> listAccounts() {
        ArrayList<Account> result = accounts.findAll();
        result.sort(Comparator.comparingDouble(Account::getBalance)
                .reversed().thenComparing(Account::getAccountNumber));
        return result;
    }

    private static String normalizeAccountNumber(String accountNumber) {
        return accountNumber == null
                ? null : accountNumber.trim().toUpperCase(Locale.ROOT);
    }
}
