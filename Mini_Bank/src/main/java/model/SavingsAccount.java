package model;

public class SavingsAccount extends Account implements InterestBearing {

    private static final long serialVersionUID = 1L;
    private final long minBalance;

    public SavingsAccount(String owner, long opening, long minBalance) {
        super(owner, opening);
        if (minBalance < 0) {
            throw new IllegalArgumentException("Minimum balance cannot be negative");
        
        }this.minBalance = minBalance;
    }

    public long getMinBalance() {
        return minBalance;
    }

    @Override
    public double interestRate() {
        return 4.0;
    }

    @Override
    public boolean canWithdraw(long amount) {
        return amount > 0 && balanceUnsafe() - amount >= minBalance;
    }
}
