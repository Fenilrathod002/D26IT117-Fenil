package service;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.CountDownLatch;
import exception.AccountNotFoundException;
import exception.BankException;
import exception.InsufficientFundsException;
import model.Account;
import model.CurrentAccount;
import model.Customer;
import model.FixedDepositAccount;
import model.Premium;
import model.SavingsAccount;
import model.UnsafeAccountWorker;

public class MiniBank {

    private static final Path DATA_DIRECTORY = Path.of("data");
    private static final Path ACCOUNT_FILE = DATA_DIRECTORY.resolve("accounts.dat");
    private static final Path LOGS_DIRECTORY = DATA_DIRECTORY.resolve("logs");
    private static final Path TRANSACTION_FILE = LOGS_DIRECTORY.resolve("transactions.log");
    private static final Path REPORT_FILE = DATA_DIRECTORY.resolve("reports/end-of-day-report.txt");

    private record BankInfo(String name, String branch) {
        @Override
        public String toString() {
            return name + " - " + branch;
        }
    }

    private enum MenuOption {
        OPEN_ACCOUNT, DEPOSIT, WITHDRAW, TRANSFER, LIST_ACCOUNTS, STATEMENT,
        TRANSACTION_BATCH, REPORT, HISTORY, EXIT
    }

    public static void main(String[] args) {
        AccountStore accounts = loadAccounts();
        loadTransactionHistory(accounts);
        Repository<Account> repository = new Repository<>();
        repository.saveAll(accounts.getAll().toArray(Account[]::new));
        BankService bankService = new BankService(repository);
        List<Customer> customers = new ArrayList<>();
        BankInfo bank = new BankInfo("MiniBank", "Charusat Branch");

        System.out.println("========================================");
        System.out.println("Welcome to " + bank);
        System.out.println("Your everyday banking, made simple");
        System.out.println("========================================");
        System.out.println("\nSelect an option to continue.");

        try (BankSession session = new BankSession()) {
            runMenu(session.scanner(), accounts, customers, bankService);
        }
        saveAccounts(bankService.listAccounts());
    }

    private static AccountStore loadAccounts() {
        AccountStore store = new AccountStore();
        try {
            Account[] savedAccounts = StatePersister.load(ACCOUNT_FILE);
            if (savedAccounts.length > 0) {
                for (Account account : savedAccounts) {
                    store.add(account);
                }
                Account.synchronizeAccountNumberSequence(store.getAll());
                return store;
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Saved account data could not be loaded. Starting with sample accounts.");
        }

        store.add(new SavingsAccount("Dhruva", 5000, 1000));
        store.add(new CurrentAccount("Bhakti", 2000, 5000));
        store.add(new FixedDepositAccount("Aarya", 10000));
        Account.synchronizeAccountNumberSequence(store.getAll());
        return store;
    }

    private static void loadTransactionHistory(AccountStore accounts) {
        if (Files.exists(TRANSACTION_FILE)) {
            try {
                for (String line : Files.readAllLines(TRANSACTION_FILE)) {
                    accounts.recordTransaction(line);
                }
            } catch (IOException e) {
                System.out.println("Could not load transaction history: " + e.getMessage());
            }
        }
    }

    private static void saveAccounts(List<Account> accounts) {
        try {
            StatePersister.save(accounts.toArray(Account[]::new), ACCOUNT_FILE);
            System.out.println("Your account data has been saved.");
        } catch (IOException e) {
            System.out.println("Could not save accounts: " + e.getMessage());
        }
    }

    private static void runMenu(Scanner input, AccountStore accounts,
                                List<Customer> customers, BankService bankService) {
        boolean running = true;
        while (running) {
            printMenu();
            int choice = readInt(input, "Choose an option: ");
            MenuOption option = switch (choice) {
                case 1 -> MenuOption.OPEN_ACCOUNT;
                case 2 -> MenuOption.DEPOSIT;
                case 3 -> MenuOption.WITHDRAW;
                case 4 -> MenuOption.TRANSFER;
                case 5 -> MenuOption.LIST_ACCOUNTS;
                case 6 -> MenuOption.STATEMENT;
                case 7 -> MenuOption.TRANSACTION_BATCH;
                case 8 -> MenuOption.REPORT;
                case 9 -> MenuOption.HISTORY;
                case 10 -> MenuOption.EXIT;
                default -> null;
            };

            if (option == null) {
                System.out.println("Invalid choice. Enter a number from 1 to 10.");
                continue;
            }

            try {
                switch (option) {
                    case OPEN_ACCOUNT -> openAccount(input, accounts, customers, bankService);
                    case DEPOSIT -> deposit(input, accounts, bankService);
                    case WITHDRAW -> withdraw(input, accounts, bankService);
                    case TRANSFER -> transfer(input, accounts, bankService);
                    case LIST_ACCOUNTS -> listAccounts(bankService);
                    case STATEMENT -> printStatement(input, bankService);
                    case TRANSACTION_BATCH -> processDepositBatch(input, accounts, bankService);
                    case REPORT -> generateReport();
                    case HISTORY -> printTransactionHistory(accounts);
                    case EXIT -> {
                        running = false;
                        System.out.println("Thank you for banking with MiniBank. Goodbye!");
                    }
                }
            } catch (InsufficientFundsException e) {
                System.out.println("Transaction failed: short by " + e.getShortfall() + " rupees.");
            } catch (AccountNotFoundException e) {
                System.out.println(e.getMessage());
            } catch (BankException e) {
                System.out.println("Transaction failed: " + e.getMessage());
            } catch (IOException e) {
                System.out.println("File operation failed: " + e.getMessage());
            }
        }
    }

    private static void generateReport() throws IOException {
        String report = ReportGenerator.generate(LOGS_DIRECTORY, REPORT_FILE);
        System.out.print(report);
        System.out.println("Report saved to " + REPORT_FILE + ".");
    }

    private static void runRepositoryDemo(AccountStore accounts) {
        Repository<Account> accountRepository = new Repository<>();
        ArrayList<Account> currentAccounts = accounts.getAll();
        accountRepository.saveAll(currentAccounts.toArray(Account[]::new));

        if (currentAccounts.isEmpty()) {
            System.out.println("No accounts are available for the repository demo.");
            return;
        }

        Account target = currentAccounts.get(0);
        System.out.println("\nRepository lookup for " + target.getAccountNumber() + ": "
                + accountRepository.findById(target.getAccountNumber()));
        ArrayList<Account> savedAccounts = accountRepository.findAll();
        savedAccounts.sort(null);
        System.out.println("Accounts stored in the repository:");
        for (Account account : savedAccounts) {
            System.out.println(account);
        }

        Repository<Customer> customerRepository = new Repository<>();
        Customer sampleCustomer = new Customer(
                "Repository Demo", "demo@example.com", "9876543210");
        customerRepository.save(sampleCustomer);
        System.out.println("Customer repository lookup: "
                + customerRepository.findById(sampleCustomer.getCustomerId()).getName());

        Double[] balances = currentAccounts.stream()
                .map(Account::getBalance).toArray(Double[]::new);
        System.out.printf("Average account balance: %.2f%n", Repository.average(balances));
    }

    private static void runConcurrencyDemo() {
        final int workerCount = 10;
        final int depositsPerWorker = 1000;
        final long depositAmount = 1;
        long expectedBalance = (long) workerCount * depositsPerWorker * depositAmount;

        System.out.println("\nThread Safety Demo");
        System.out.println("Workers: " + workerCount + ", deposits per worker: "
                + depositsPerWorker + ", amount: " + depositAmount);

        Account raceAccount = new SavingsAccount("Race Demo", 0, 0);
        try {
            runWorkers(raceAccount, workerCount, depositsPerWorker, depositAmount, false);
            System.out.println("Balance before synchronization: "
                    + raceAccount.getBalance() + " (expected " + expectedBalance + ")");

            Account safeAccount = new SavingsAccount("Synchronized Demo", 0, 0);
            runWorkers(safeAccount, workerCount, depositsPerWorker, depositAmount, true);
            System.out.println("Balance after synchronization: "
                    + safeAccount.getBalance() + " (expected " + expectedBalance + ")");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Concurrency demo interrupted.");
        }
    }

    private static void runWorkers(Account account, int workerCount, int times,
                                   long amount, boolean synchronizedDeposit)
            throws InterruptedException {
        Thread[] workers = new Thread[workerCount];
        for (int i = 0; i < workerCount; i++) {
            Runnable task = synchronizedDeposit
                    ? new AccountWorker(account, times, amount)
                    : new UnsafeAccountWorker(account, times, amount);
            String prefix = synchronizedDeposit ? "safe-deposit-" : "race-deposit-";
            workers[i] = new Thread(task, prefix + (i + 1));
            workers[i].start();
        }
        for (Thread worker : workers) {
            worker.join();
        }
    }

    private static void processDepositBatch(Scanner input, AccountStore accounts,
                                           BankService bankService) throws BankException {
        Account account = findAccount(input, bankService, "Account number: ");
        int transactionCount = readInt(input, "Number of deposits to process (1-1000): ");
        if (transactionCount < 1 || transactionCount > 1000) {
            System.out.println("Enter a batch size from 1 to 1000.");
            return;
        }
        long amountPerTransaction = readLong(input, "Amount for each deposit (rupees): ");
        if (amountPerTransaction <= 0) {
            System.out.println("Deposit amount must be greater than zero.");
            return;
        }

        double startingBalance = account.getBalance();
        TransactionProcessor processor = new TransactionProcessor(bankService.listAccounts());
        Account sharedAccount = processor.getAccount(account.getAccountNumber());
        TransactionBuffer buffer = new TransactionBuffer(10);
        Thread producer = new Thread(() -> {
            try {
                for (int i = 0; i < transactionCount; i++) {
                    buffer.put(new AccountWorker(sharedAccount, 1, amountPerTransaction));
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }, "deposit-producer");
        Thread consumer = new Thread(() -> {
            try {
                for (int i = 0; i < transactionCount; i++) {
                    processor.submit(buffer.take());
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }, "deposit-consumer");

        try {
            producer.start();
            consumer.start();
            producer.join();
            consumer.join();
            processor.stop();
            for (int i = 0; i < transactionCount; i++) {
                appendTransaction("DEPOSIT " + account.getAccountNumber() + " "
                        + amountPerTransaction, accounts);
            }
            System.out.printf("Batch completed successfully: %d deposits of ₹%,d.%n",
                    transactionCount, amountPerTransaction);
            System.out.printf("Updated balance: ₹%,.2f (was ₹%,.2f)%n",
                    account.getBalance(), startingBalance);
        } catch (InterruptedException e) {
            processor.stopNow();
            Thread.currentThread().interrupt();
            System.out.println("The deposit batch was interrupted. Please check the account balance.");
        }
    }

    private static void runDeadlockAndLockOrderingDemo() {
        Account unsafeA = new CurrentAccount("Unsafe A", 1000, 5000);
        Account unsafeB = new CurrentAccount("Unsafe B", 1000, 5000);
        CountDownLatch bothThreadsHoldFirstLock = new CountDownLatch(2);

        Thread unsafeTransferOne = new Thread(
                () -> transferWithOppositeLockOrder(unsafeA, unsafeB, bothThreadsHoldFirstLock),
                "unsafe-A-to-B");
        Thread unsafeTransferTwo = new Thread(
                () -> transferWithOppositeLockOrder(unsafeB, unsafeA, bothThreadsHoldFirstLock),
                "unsafe-B-to-A");
        unsafeTransferOne.setDaemon(true);
        unsafeTransferTwo.setDaemon(true);
        unsafeTransferOne.start();
        unsafeTransferTwo.start();

        try {
            unsafeTransferOne.join(500);
            unsafeTransferTwo.join(500);
            System.out.println("Opposite lock order deadlocked: "
                    + (unsafeTransferOne.isAlive() && unsafeTransferTwo.isAlive()));

            Account safeA = new CurrentAccount("Safe A", 1000, 5000);
            Account safeB = new CurrentAccount("Safe B", 1000, 5000);
            Thread safeTransferOne = new Thread(
                    () -> transferWithOrderedLocks(safeA, safeB, 500), "safe-A-to-B");
            Thread safeTransferTwo = new Thread(
                    () -> transferWithOrderedLocks(safeB, safeA, 500), "safe-B-to-A");
            safeTransferOne.start();
            safeTransferTwo.start();
            safeTransferOne.join();
            safeTransferTwo.join();
            System.out.println("Ordered-lock transfers completed. Final balances: "
                    + safeA.getBalance() + " and " + safeB.getBalance());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Deadlock demonstration interrupted.");
        }
    }

    private static void transferWithOppositeLockOrder(Account from, Account to,
                                                      CountDownLatch ready) {
        synchronized (from) {
            ready.countDown();
            try {
                ready.await();
                synchronized (to) {
                    from.transfer(to, 100);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } catch (BankException e) {
                System.out.println(Thread.currentThread().getName()
                        + " failed: " + e.getMessage());
            }
        }
    }

    private static void transferWithOrderedLocks(Account from, Account to, long amount) {
        Account first = from.getAccountNumber().compareTo(to.getAccountNumber()) < 0
                ? from : to;
        Account second = first == from ? to : from;
        synchronized (first) {
            synchronized (second) {
                try {
                    from.transfer(to, amount);
                    System.out.println(Thread.currentThread().getName() + " completed.");
                } catch (BankException e) {
                    System.out.println(Thread.currentThread().getName()
                            + " failed: " + e.getMessage());
                }
            }
        }
    }

    private static void printMenu() {
        System.out.println("Main Menu");
        System.out.println("---------");
        System.out.println("1. Open account");
        System.out.println("2. Deposit");
        System.out.println("3. Withdraw");
        System.out.println("4. Transfer");
        System.out.println("5. List accounts");
        System.out.println("6. View statement");
        System.out.println("7. Process deposit batch");
        System.out.println("8. Generate end-of-day report");
        System.out.println("9. View transaction history");
        System.out.println("10. Exit");
    }

    private static void openAccount(Scanner input, AccountStore accounts,
                                    List<Customer> customers, BankService bankService)
            throws BankException {
        System.out.println("Account type: 1. Savings  2. Current  3. Fixed Deposit");
        int type = readInt(input, "Choose account type: ");
        System.out.print("Owner name: ");
        String owner = input.nextLine().trim();
        System.out.print("Email address: ");
        String email = input.nextLine().trim();
        System.out.print("Mobile number: ");
        String mobile = input.nextLine().trim();
        long openingBalance = readLong(input, "Opening balance (rupees): ");
        Account account = bankService.openAccount(owner, email, mobile, type, openingBalance);
        Customer customer = new Customer(owner, email, mobile);
        customers.add(customer);
        accounts.add(account);
        if (openingBalance > 0) {
            appendTransaction("DEPOSIT " + account.getAccountNumber() + " "
                    + (long) openingBalance, accounts);
        }
        System.out.println("Customer created: " + customer.getCustomerId());
        System.out.println("Account opened: " + account);
        System.out.printf("Interest rate: %.1f%% per year%n", account.interestRate());
    }

    private static void deposit(Scanner input, AccountStore accounts, BankService bankService)
            throws BankException {
        Account account = findAccount(input, bankService, "Account number: ");
        long amount = readLong(input, "Deposit amount: ");
        bankService.deposit(account.getAccountNumber(), amount);
        appendTransaction("DEPOSIT " + account.getAccountNumber() + " " + amount, accounts);
        System.out.println("Deposit successful. New balance: " + account.getBalance());
    }

    private static void withdraw(Scanner input, AccountStore accounts, BankService bankService)
            throws BankException {
        Account account = findAccount(input, bankService, "Account number: ");
        long amount = readLong(input, "Withdrawal amount: ");
        WithdrawRule anonymousRule = new WithdrawRule() {
            @Override
            public boolean allow(Account candidate, long requestedAmount) {
                return requestedAmount > 0 && candidate.canWithdraw(requestedAmount);
            }
        };
        WithdrawRule lambdaRule = (candidate, requestedAmount) ->
                requestedAmount > 0 && candidate.canWithdraw(requestedAmount);

        boolean anonymousAllowed = anonymousRule.allow(account, amount);
        boolean lambdaAllowed = lambdaRule.allow(account, amount);
        if (anonymousAllowed && lambdaAllowed) {
            bankService.withdraw(account.getAccountNumber(), amount);
            appendTransaction("WITHDRAW " + account.getAccountNumber() + " " + amount,
                    accounts);
            System.out.println("Withdrawal successful. New balance: " + account.getBalance());
        } else {
            bankService.withdraw(account.getAccountNumber(), amount);
        }
    }

    private static void transfer(Scanner input, AccountStore accounts, BankService bankService)
            throws BankException {
        Account from = findAccount(input, bankService, "From account number: ");
        Account to = findAccount(input, bankService, "To account number: ");
        long amount = readLong(input, "Transfer amount: ");
        bankService.transfer(from.getAccountNumber(), to.getAccountNumber(), amount);
        appendTransaction("WITHDRAW " + from.getAccountNumber() + " " + amount, accounts);
        appendTransaction("DEPOSIT " + to.getAccountNumber() + " " + amount, accounts);
        System.out.println("Transfer successful.");
    }

    private static void appendTransaction(String entry, AccountStore accounts) {
        accounts.recordTransaction(entry);
        try {
            TransactionLog.append(TRANSACTION_FILE, entry);
        } catch (IOException e) {
            System.out.println("Warning: transaction completed but could not be logged: "
                    + e.getMessage());
        }
    }

    private static void listAccounts(BankService bankService) {
        System.out.println("\nAccount list:");
        printAccounts(bankService.listAccounts());
    }

    private static void printAccounts(List<Account> accounts) {
        for (Account account : accounts) {
            System.out.printf("%s | interest %.1f%% per year | estimated yearly interest: %.2f%n",
                    account, account.interestRate(), account.yearlyInterest());
            if (account instanceof Premium) {
                System.out.println("  Premium account");
            }
            if (account instanceof FixedDepositAccount fixedDeposit) {
                System.out.println("  Fixed deposit is locked; withdrawals are not allowed.");
            }
        }
    }

    private static void printStatement(Scanner input, BankService bankService)
            throws AccountNotFoundException {
        Account account = findAccount(input, bankService, "Account number: ");
        System.out.print(bankService.statement(account.getAccountNumber()));
        System.out.printf("Yearly interest estimate: %.2f%n", account.yearlyInterest());
    }

    private static Account findAccount(Scanner input, BankService bankService, String prompt)
            throws AccountNotFoundException {
        System.out.print(prompt);
        String accountNumber = input.nextLine().trim();
        return bankService.getAccount(accountNumber);
    }

    private static void printTransactionHistory(AccountStore accounts) {
        ArrayList<String> history = accounts.getTransactionHistory();
        System.out.println("\nTransaction history:");
        if (history.isEmpty()) {
            System.out.println("No transactions recorded.");
            return;
        }
        for (String transaction : history) {
            System.out.println(transaction);
        }
    }

    private static int readInt(Scanner input, String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Integer.parseInt(input.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a whole number.");
            }
        }
    }

    private static long readLong(Scanner input, String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Long.parseLong(input.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a whole number.");
            }
        }
    }
}
