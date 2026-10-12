package util;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

public final class ReportGenerator {

    private ReportGenerator() {
    }

    public static Path generate(Path logs, Path report) throws IOException {
        Files.createDirectories(logs);
        long deposits = 0, withdrawals = 0, malformed = 0;
        StringBuilder details = new StringBuilder();
        try (DirectoryStream<Path> ds = Files.newDirectoryStream(logs, "*.log")) {
            for (Path file : ds) {
                BasicFileAttributes a = Files.readAttributes(file, BasicFileAttributes.class);
                details.append(file.getFileName()).append(" size=").append(a.size()).append(" modified=").append(a.lastModifiedTime()).append('\n');
                try (BufferedReader r = Files.newBufferedReader(file, StandardCharsets.UTF_8)) {
                    String line;
                    while ((line = r.readLine()) != null) {
                        if (line.isBlank()) {
                            continue;
                        
                        }String[] p = line.split("\\|", -1);
                        try {
                            if (p.length != 4 || Long.parseLong(p[3]) <= 0) {
                                throw new IllegalArgumentException();
                            
                            }if ("DEPOSIT".equals(p[1])) {
                                deposits = Math.addExact(deposits, Long.parseLong(p[3])); 
                            }else if ("WITHDRAW".equals(p[1])) {
                                withdrawals = Math.addExact(withdrawals, Long.parseLong(p[3])); 
                            }else {
                                throw new IllegalArgumentException();
                        
                            }} catch (IllegalArgumentException | ArithmeticException e) {
                            malformed++;
                        }
                    }
                }
            }
        }
        Path parent = report.toAbsolutePath().getParent();
        if (parent != null) {
            Files.createDirectories(parent);
        
        }Files.writeString(report, "END-OF-DAY REPORT\nDeposits: " + deposits + "\nWithdrawals: " + withdrawals + "\nMalformed lines skipped: " + malformed + "\n" + details, StandardCharsets.UTF_8);
        return report;
    }
}
