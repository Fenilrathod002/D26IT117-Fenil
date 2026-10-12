package util;

import model.Account;

public final class StatementFormatter {

    private StatementFormatter() {
    }

    public static String buildStatement(Account a) {
        return new StringBuilder("--- Account Statement ---\n").append("Number: ").append(a.getAccountNumber()).append("\nOwner: ").append(a.getOwnerName()).append("\nBalance: Rs. ").append(a.getBalance()).append("\nStatus: ").append(a.isActive() ? "Active" : "Inactive").append('\n').toString();
    }
}
