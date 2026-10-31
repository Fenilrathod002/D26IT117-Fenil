package app;

import java.util.Scanner;
import model.BankAccount;
import service.BankService;

public class BankMenu {
    public static void main(String[] args) {
        BankService service = new BankService();
        try (Scanner input = new Scanner(System.in)) {
            boolean running = true;
            while (running) {
                printMenu();
                int action;
                try {
                    action = Integer.parseInt(input.nextLine().trim());
                    service.validateAction(action);
                } catch (IllegalArgumentException e) {
                    System.out.println("Invalid action. Enter 1, 2, or 3.");
                    continue;
                }

                switch (action) {
                    case 1 -> addAccount(input, service);
                    case 2 -> service.listAccounts().forEach(System.out::println);
                    case 3 -> {
                        running = false;
                        System.out.println("Goodbye.");
                    }
                    default -> throw new IllegalStateException("Validated action was unexpected.");
                }
            }
        }
    }

    private static void printMenu() {
        System.out.println("\nSimple Bank Menu");
        System.out.println("1. Add account");
        System.out.println("2. List accounts");
        System.out.println("3. Exit");
        System.out.print("Choose an action: ");
    }

    private static void addAccount(Scanner input, BankService service) {
        try {
            System.out.print("Account number: ");
            String number = input.nextLine().trim();
            System.out.print("Owner name: ");
            String owner = input.nextLine().trim();
            System.out.print("Opening balance: ");
            long balance = Long.parseLong(input.nextLine().trim());
            BankAccount account = service.addAccount(number, owner, balance);
            System.out.println("Account added: " + account);
        } catch (IllegalArgumentException e) {
            System.out.println("Could not add account: " + e.getMessage());
        }
    }
}
