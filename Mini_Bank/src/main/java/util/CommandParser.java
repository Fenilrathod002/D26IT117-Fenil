package util;

import model.*;

public final class CommandParser {

    private CommandParser() {
    }

    public static Command parse(String line) {
        if (line == null) {
            throw new IllegalArgumentException("Command is required");
        
        }String[] p = line.trim().split("\\s+");
        if (p.length != 3) {
            throw new IllegalArgumentException("Expected TYPE ACCOUNT AMOUNT");
        
        }try {
            TransactionType type = TransactionType.valueOf(p[0].toUpperCase());
            if (!p[1].matches("AC\\d+")) {
                throw new IllegalArgumentException("Invalid account number");
            
            }if (!Validator.isValidAmount(p[2])) {
                throw new IllegalArgumentException("Amount must be a positive whole number");
            
            }return new Command(type, p[1], Long.parseLong(p[2]));
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("Invalid command: " + ex.getMessage(), ex);
        }
    }
}
