import java.util.Comparator;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class WordFrequency {
    public static void main(String[] args) {
        String paragraph = "Java makes reliable software. Java collections make software "
                + "easier to organize, and reliable code is easier to maintain.";
        Map<String, Integer> frequencies = new HashMap<>();

        for (String word : paragraph.toLowerCase(Locale.ROOT).split("[^\\p{L}\\p{N}]+")) {
            if (!word.isBlank()) {
                frequencies.merge(word, 1, Integer::sum);
            }
        }

        System.out.println("Word frequencies (highest first):");
        frequencies.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue(Comparator.reverseOrder())
                        .thenComparing(Map.Entry.comparingByKey()))
                .forEach(entry -> System.out.printf("%s: %d%n", entry.getKey(), entry.getValue()));
    }
}
