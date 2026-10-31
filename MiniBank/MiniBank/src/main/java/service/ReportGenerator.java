package service;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Locale;

public final class ReportGenerator {
    private ReportGenerator() { }

    public static String generate(Path logsDirectory, Path reportFile) throws IOException {
        Files.createDirectories(logsDirectory);
        long totalDeposits = 0;
        long totalWithdrawals = 0;
        int malformedLines = 0;
        StringBuilder report = new StringBuilder("MiniBank End-of-Day Report\n");
        report.append("Log files:\n");

        try (DirectoryStream<Path> files = Files.newDirectoryStream(logsDirectory)) {
            for (Path file : files) {
                if (!Files.isRegularFile(file)) {
                    continue;
                }
                BasicFileAttributes attributes = Files.readAttributes(
                        file, BasicFileAttributes.class);
                report.append("  ").append(file.getFileName())
                        .append(" | ").append(attributes.size()).append(" bytes\n");

                try (BufferedReader reader = Files.newBufferedReader(file, StandardCharsets.UTF_8)) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        String[] fields = line.trim().split("\\s+");
                        if (fields.length < 3) {
                            malformedLines++;
                            continue;
                        }
                        long amount;
                        try {
                            amount = Long.parseLong(fields[2]);
                        } catch (NumberFormatException e) {
                            malformedLines++;
                            continue;
                        }
                        if (amount < 0) {
                            malformedLines++;
                            continue;
                        }
                        switch (fields[0].toUpperCase(Locale.ROOT)) {
                            case "DEPOSIT" -> totalDeposits += amount;
                            case "WITHDRAW" -> totalWithdrawals += amount;
                            default -> malformedLines++;
                        }
                    }
                }
            }
        }

        report.append("Total deposits: ").append(totalDeposits).append(" rupees\n")
                .append("Total withdrawals: ").append(totalWithdrawals).append(" rupees\n")
                .append("Net change: ").append(totalDeposits - totalWithdrawals)
                .append(" rupees\n")
                .append("Unrecognized log lines: ").append(malformedLines).append('\n');

        Path parent = reportFile.toAbsolutePath().getParent();
        if (parent != null) {
            Files.createDirectories(parent);
        }
        Files.writeString(reportFile, report.toString(), StandardCharsets.UTF_8,
                StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        return report.toString();
    }
}
