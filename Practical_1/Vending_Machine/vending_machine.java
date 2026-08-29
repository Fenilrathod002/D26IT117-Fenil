import java.util.Scanner;

public class vending_machine {

    enum Coin {
        ONE, TWO, FIVE, TEN
    }

    public static void main(String[] args) {

        final int price = 15;
        int total = 0;

        Scanner scanner = new Scanner(System.in);

        while (total < price) {

            System.out.print("Enter coin (ONE, TWO, FIVE, TEN): ");

            Coin selectedCoin = Coin.valueOf(
                scanner.nextLine().toUpperCase()
            );

            int coinValue = switch (selectedCoin) {
                case ONE -> 1;
                case TWO -> 2;
                case FIVE -> 5;
                case TEN -> 10;
            };

            total += coinValue;

            System.out.println("Total So Far: " + total);
        }

        System.out.println("Paid. Change: " + (total - price));

    }
}