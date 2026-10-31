import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;
import java.util.Locale;

public class LogAnalyzer {
    private static final String KEYWORD = "ERROR";

    public static void main(String[] args) throws IOException {
        Path logsDirectory = Path.of("data", "logs");
        List<Path> files;
        try (var paths = Files.list(logsDirectory)) {
            files = paths.filter(Files::isRegularFile).sorted().toList();
        }

        int totalLines = 0;
        int matchingLines = 0;
        for (Path file : files) {
            BasicFileAttributes attributes = Files.readAttributes(file, BasicFileAttributes.class);
            int fileLines = 0;
            int fileMatches = 0;
            try (BufferedReader reader = Files.newBufferedReader(file)) {
                String line;
                while ((line = reader.readLine()) != null) {
                    fileLines++;
                    if (line.toLowerCase(Locale.ROOT).contains(KEYWORD.toLowerCase(Locale.ROOT))) {
                        fileMatches++;
                    }
                }
            }
            totalLines += fileLines;
            matchingLines += fileMatches;
            System.out.printf("%s: %d bytes, %d lines, %d containing %s%n",
                    file.getFileName(), attributes.size(), fileLines, fileMatches, KEYWORD);
        }

        System.out.println("Files analyzed: " + files.size());
        System.out.println("Total lines: " + totalLines);
        System.out.println("Lines containing " + KEYWORD + ": " + matchingLines);
    }
}
