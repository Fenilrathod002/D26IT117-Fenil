
package model;

import java.io.Serial;
import java.io.Serializable;

public class SavingsAccount extends Account implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private long minBalance;

    public SavingsAccount(String ownerName, double balance,
                          long minBalance) {
        super(ownerName, balance);
        this.minBalance = minBalance;
    }

    @Override
    public double interestRate() {
        return 4.0;
    }

    @Override
    public synchronized boolean canWithdraw(long amount) {
        return amount >= 0 && balance - amount >= minBalance;
    }
}
