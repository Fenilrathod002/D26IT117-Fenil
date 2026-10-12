package util;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import static java.nio.file.StandardOpenOption.*;

public final class TransactionLog {

    private TransactionLog() {
    }

    public static void append(Path file, String line) throws IOException {
        Path p = file.toAbsolutePath().getParent();
        if (p != null) {
            Files.createDirectories(p);
        
        }Files.writeString(file, line + System.lineSeparator(), StandardCharsets.UTF_8, CREATE, APPEND);
    }
}
