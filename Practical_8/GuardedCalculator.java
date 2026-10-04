import java.util.Scanner;

class DivideByZeroException extends Exception {
    public DivideByZeroException(String message) {
        super(message);
    }
}

public class GuardedCalculator {
    public static double calculate(double A, double B, char OP)
            throws DivideByZeroException {

        switch (OP) {
            case '+':
                return A + B;

            case '-':
                return A - B;

            case '*':
                return A * B;

            case '/':
                if (B == 0) {
                    throw new DivideByZeroException("Cannot divide by zero.");
                }
                return A / B;
        }
        return 0;
    }

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        boolean succeed = false;

        while (!succeed) {

            try {
                System.out.print("Enter first number: ");
                double A = sc.nextDouble();

                System.out.print("Enter second number: ");
                double B = sc.nextDouble();

                System.out.print("Enter operator (+, -, *, /): ");
                char OP = sc.next().charAt(0);

                double result = calculate(A, B, OP);
                System.out.println("Result = " + result);
                succeed = true;
            }

            catch (java.util.InputMismatchException e) {
                System.out.println("Invalid number input. Please enter numbers only.");
                sc.nextLine();
            }
            catch (DivideByZeroException e) {
                System.out.println("Error: " + e.getMessage());
            }
            finally {
                System.out.println("Attempt completed.");
            }
        }
        sc.close();
    }
}


