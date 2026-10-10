import java.util.Scanner;

/**
 * A simple console menu shell for the MiniBank practical.
 */
public class MiniBank {
    public static void main(String[] args) {
        BankInfo bankInfo = new BankInfo("MiniBank", "Main Branch");

        System.out.println("========================================");
        System.out.println("Welcome to " + bankInfo.name());
        System.out.println("Branch: " + bankInfo.branch());
        System.out.println("========================================");

        try (Scanner scanner = new Scanner(System.in)) {
            boolean isRunning = true;

            while (isRunning) {
                printMenu();

                if (!scanner.hasNextLine()) {
                    System.out.println("No more input available. Goodbye!");
                    break;
                }

                String input = scanner.nextLine().trim();

                if (input.isEmpty()) {
                    System.out.println("Invalid input. Please enter a number from 1 to 6.");
                    continue;
                }

                try {
                    int selection = Integer.parseInt(input);

                    // A switch expression maps each menu number to its enum option.
                    MenuOption selectedOption = switch (selection) {
                        case 1 -> MenuOption.OPEN_ACCOUNT;
                        case 2 -> MenuOption.DEPOSIT;
                        case 3 -> MenuOption.WITHDRAW;
                        case 4 -> MenuOption.TRANSFER;
                        case 5 -> MenuOption.EXIT;
                        case 6 -> MenuOption.WORKING_HOURS;
                        default -> null;
                    };

                    if (selectedOption == null) {
                        System.out.println("Invalid option. Please enter a number from 1 to 6.");
                        continue;
                    }

                    switch (selectedOption) {
                        case OPEN_ACCOUNT -> System.out.println("Open Account - to be implemented in a later lab.");
                        case DEPOSIT -> System.out.println("Deposit - to be implemented in a later lab.");
                        case WITHDRAW -> System.out.println("Withdraw - to be implemented in a later lab.");
                        case TRANSFER -> System.out.println("Transfer - to be implemented in a later lab.");
                        case WORKING_HOURS -> System.out.println(
                                "MiniBank Working Hours: Monday to Saturday, 9:00 AM to 5:00 PM.");
                        case EXIT -> {
                            System.out.println("Goodbye! Thank you for using MiniBank.");
                            isRunning = false;
                        }
                    }
                } catch (NumberFormatException exception) {
                    System.out.println("Invalid input. Please enter a number from 1 to 6.");
                }
            }
        }
    }

    private static void printMenu() {
        System.out.println();
        System.out.println("========== MINIBANK MENU ==========");
        System.out.println("1. Open Account");
        System.out.println("2. Deposit");
        System.out.println("3. Withdraw");
        System.out.println("4. Transfer");
        System.out.println("5. Exit");
        System.out.println("6. Bank Working Hours");
        System.out.print("Choose an option: ");
    }
}
