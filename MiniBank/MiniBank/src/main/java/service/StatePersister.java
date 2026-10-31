package service;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import model.Account;

public final class StatePersister {
    private StatePersister() { }

    public static void save(Account[] accounts, Path file) throws IOException {
        Path parent = file.toAbsolutePath().getParent();
        if (parent != null) {
            Files.createDirectories(parent);
        }
        try (ObjectOutputStream output = new ObjectOutputStream(Files.newOutputStream(file))) {
            output.writeObject(accounts);
        }
    }

    public static Account[] load(Path file) throws IOException, ClassNotFoundException {
        if (Files.notExists(file)) {
            return new Account[0];
        }
        try (ObjectInputStream input = new ObjectInputStream(Files.newInputStream(file))) {
            Object savedState = input.readObject();
            if (!(savedState instanceof Account[] accounts)) {
                throw new IOException("Saved bank data has an unexpected format.");
            }
            return accounts;
        }
    }
}
