
package model;

import java.io.Serial;
import java.io.Serializable;
import exception.InvalidAmountException;

public class FixedDepositAccount extends Account implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    public FixedDepositAccount(String ownerName, double balance) {
        super(ownerName, balance);
    }

    @Override
    public double interestRate() {
        return 7.0;
    }

    @Override
    public boolean canWithdraw(long amount) {
        return false;
    }

    @Override
    public boolean withdraw(long amount) throws InvalidAmountException {
        if (amount <= 0) {
            throw new InvalidAmountException("Withdrawal amount must be greater than zero.");
        }
        throw new InvalidAmountException("Fixed deposit is locked; withdrawals are not allowed.");
    }
}
