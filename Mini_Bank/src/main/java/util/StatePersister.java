package util;

import java.io.*;
import java.nio.file.*;
import model.Account;

public final class StatePersister {

    private StatePersister() {
    }

    public static void save(Account[] accounts, Path file) throws IOException {
        Path parent = file.toAbsolutePath().getParent();
        if (parent != null) {
            Files.createDirectories(parent);
        
        }try (ObjectOutputStream out = new ObjectOutputStream(Files.newOutputStream(file))) {
            out.writeObject(accounts);
        }
    }

    public static Account[] load(Path file) throws IOException {
        if (!Files.exists(file)) {
            throw new FileNotFoundException("State file not found: " + file);
        
        }try (ObjectInputStream in = new ObjectInputStream(Files.newInputStream(file))) {
            Object value = in.readObject();
            if (!(value instanceof Account[] accounts)) {
                throw new IOException("Unexpected state data");
            
            }return accounts;
        } catch (ClassNotFoundException | ClassCastException e) {
            throw new IOException("Corrupt or incompatible state file", e);
        }
    }
}
