
import exception.*;
import model.*;
import model.annotation.MaxLength;
import model.annotation.Positive;
import service.*;
import util.*;
import java.io.IOException;
import java.nio.file.*;
import java.time.Instant;
import java.util.*;
import static util.Validator.isValidMobile;

/**
 * One clear console entry point for the integrated MiniBank practicals.
 */
public class MiniBank {

    private static final Map<String, Account> accounts = new LinkedHashMap<>();
    private static final BankInfo BANK = new BankInfo("MiniBank", "CSPIT Branch");

    public static void main(String[] args) {
        if (Arrays.asList(args).contains("--demo")) {
            runDemo();
            return;
        }
        interactive();
    }

    private static void interactive() {
        System.out.println(BANK);
        try (Scanner scanner = new Scanner(System.in)) {
            boolean running = true;
            while (running) {
                System.out.println("\n1.Open 2.Deposit 3.Withdraw 4.Transfer 5.Working hours 6.Exit");
                System.out.print("Choice: ");
                String input = scanner.nextLine().trim();
                try {
                    int choice = Integer.parseInt(input);
                    String result = switch (choice) {
                        case 1 ->
                            open(scanner);
                        case 2 ->
                            deposit(scanner);
                        case 3 ->
                            withdraw(scanner);
                        case 4 ->
                            transfer(scanner);
                        case 5 ->
                            "Working hours: Monday-Saturday, 10:00 to 16:00.";
                        case 6 -> {
                            running = false;
                            yield "Goodbye from MiniBank.";
                        }
                        default ->
                            "Invalid menu number. Please choose 1-6.";
                    };
                    System.out.println(result);
                } catch (NumberFormatException e) {
                    System.out.println("Enter a numbered menu option.");
                }
            }
        }
    }

    private static String open(Scanner s) {
        System.out.print("Owner name: ");
        String owner = s.nextLine().trim();
        System.out.print("Opening balance: ");
        try {
            Account a = new SavingsAccount(owner, Long.parseLong(s.nextLine().trim()), 0);
            accounts.put(a.getAccountNumber(), a);
            return "Opened " + a;
        } catch (RuntimeException e) {
            return "Could not open account: " + e.getMessage();
        }
    }

    private static Account find(Scanner s) throws AccountNotFoundException {
        System.out.print("Account number: ");
        Account a = accounts.get(s.nextLine().trim());
        if (a == null) {
            throw new AccountNotFoundException("Account not found");
        
        }return a;
    }

    private static long amount(Scanner s) {
        System.out.print("Amount: ");
        return Long.parseLong(s.nextLine().trim());
    }

    private static String deposit(Scanner s) {
        try {
            Account a = find(s);
            a.deposit(amount(s));
            return StatementFormatter.buildStatement(a);
        } catch (BankException | NumberFormatException e) {
            return e.getMessage();
        }
    }

    private static String withdraw(Scanner s) {
        try {
            Account a = find(s);
            a.withdraw(amount(s));
            return StatementFormatter.buildStatement(a);
        } catch (BankException | NumberFormatException e) {
            return e.getMessage();
        }
    }

    private static String transfer(Scanner s) {
        try {
            System.out.println("From account");
            Account from = find(s);
            System.out.println("To account");
            Account to = find(s);
            from.transfer(to, amount(s));
            return "Transfer complete.";
        } catch (BankException | NumberFormatException e) {
            return e.getMessage();
        }
    }

    private static void runDemo() {
        System.out.println("=== " + BANK + " final practical demo ===");
        Account savings = new SavingsAccount("Asha", 5_000, 1_000);
        Account current = new CurrentAccount("Bharat", 2_000, 500);
        Account deposit = new FixedDepositAccount("Chirag", 10_000);
        accounts.put(savings.getAccountNumber(), savings);
        accounts.put(current.getAccountNumber(), current);
        accounts.put(deposit.getAccountNumber(), deposit);
        for (Account a : new Account[]{savings, current, deposit}) {
            System.out.println(a + ", rate=" + a.interestRate());
        }
        if (savings instanceof SavingsAccount sa) {
            System.out.println("Savings minimum balance: " + sa.getMinBalance());
        }
        System.out.println("Mobile valid/invalid: " + isValidMobile("9876543210") + "/" + isValidMobile("123"));
        System.out.println("Parsed command: " + CommandParser.parse("DEPOSIT " + savings.getAccountNumber() + " 500"));
        try {
            savings.deposit(500);
            savings.transfer(current, 700);
            current.withdraw(100);
        } catch (BankException e) {
            System.out.println("Banking error: " + e.getMessage());
        } finally {
            System.out.println("Transfer attempt completed.");
        }
        WithdrawRule anonymous = new WithdrawRule() {
            public boolean allow(Account a, long n) {
                return a.canWithdraw(n);
            }
        };
        WithdrawRule lambda = (a, n) -> a.canWithdraw(n);
        System.out.println("Rules agree: " + (anonymous.allow(savings, 100) == lambda.allow(savings, 100)));
        System.out.println("Annotation fixture errors: " + Arrays.toString(AnnotationValidator.validate(new InvalidFixture())));
        try (DemoResource resource = new DemoResource()) {
            System.out.println("Try-with-resources is active.");
        }
        threadDemo();
        processorDemo(savings);
        persistenceDemo(savings, current, deposit);
        System.out.println(StatementFormatter.buildStatement(savings));
    }

    private static void threadDemo() {
        Account a = new SavingsAccount("Thread Demo", 0, 0);
        List<Thread> threads = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Thread t = new Thread(new AccountWorker(a, 100, 1), "deposit-worker-" + i);
            threads.add(t);
            t.start();
        }
        for (Thread t : threads)try {
            t.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("Thread-safe expected/actual: 1000/" + a.getBalance());
    }

    private static void processorDemo(Account account) {
        TransactionProcessor p = new TransactionProcessor();
        for (int i = 0; i < 4; i++) {
            p.submit(() -> {
                try {
                    account.deposit(1);
                } catch (InvalidAmountException e) {
                    System.err.println(e.getMessage());
                }
            });
        
        }p.stop();
        TransactionBuffer b = new TransactionBuffer(2);
        Thread producer = new Thread(() -> {
            try {
                b.put(new Command(TransactionType.DEPOSIT, account.getAccountNumber(), 1));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                b.close();
            }
        });
        Thread consumer = new Thread(() -> {
            try {
                while (b.take() != null) {
                    System.out.println("Buffer transaction consumed");
            
                }} catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        producer.start();
        consumer.start();
        try {
            producer.join();
            consumer.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private static void persistenceDemo(Account... accounts) {
        try {
            Path data = Path.of("data", "accounts.bin");
            StatePersister.save(accounts, data);
            System.out.println("Reloaded accounts: " + StatePersister.load(data).length);
            Path log = Path.of("logs", "transactions.log");
            TransactionLog.append(log, Instant.now() + "|DEPOSIT|" + accounts[0].getAccountNumber() + "|500");
            TransactionLog.append(log, Instant.now() + "|WITHDRAW|" + accounts[0].getAccountNumber() + "|100");
            Path report = ReportGenerator.generate(Path.of("logs"), Path.of("reports", "end-of-day.txt"));
            System.out.println("Report: " + report);
        } catch (IOException e) {
            System.err.println("Persistence demo: " + e.getMessage());
        }
    }

    private static final class InvalidFixture {

        @Positive
        private final long amount = -1;
        @MaxLength(10)
        private final String label = "too-long-label";
    }

    private static final class DemoResource implements AutoCloseable {

        @Override
        public void close() {
            System.out.println("Demo resource closed.");
        }
    }
}
