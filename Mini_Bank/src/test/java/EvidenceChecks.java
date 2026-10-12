import exception.BankException;
import model.*;
import util.CommandParser;

/** Test-only console verifier used to capture reproducible practical evidence. */
public class EvidenceChecks {
    public static void main(String[] args) {
        Customer customer = new Customer("Riya", "riya@example.com", "9876543210", new Customer.Address("1 College Road", "Ahmedabad", "380001"));
        Customer copy = customer.clone();
        Account first = new SavingsAccount(customer.getName(), 1000, 100);
        Account second = new SavingsAccount("Other", 1000, 0);
        System.out.println("Customer ID: " + customer.getCustomerId());
        System.out.println("Clone has independent immutable address view: " + (copy.getAddress() != customer.getAddress()));
        System.out.println("Account equality: same=" + first.equals(first) + ", different=" + first.equals(second));
        System.out.println("Account text: " + first);
        try { first.deposit(250); first.withdraw(100); System.out.println("Deposit/withdraw balance: " + first.getBalance()); }
        catch (BankException e) { System.out.println("Unexpected failure: " + e.getMessage()); }
        try { first.withdraw(10_000); } catch (BankException e) { System.out.println("Expected checked exception: " + e.getMessage()); }
        try { CommandParser.parse("BAD"); } catch (IllegalArgumentException e) { System.out.println("Expected parser error: " + e.getMessage()); }
        System.out.println("Savings yearly interest: " + ((InterestBearing) first).yearlyInterest());
    }
}
