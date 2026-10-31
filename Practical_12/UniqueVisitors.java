import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class UniqueVisitors {
    public static void main(String[] args) {
        List<String> logins = Arrays.asList(
                "riya", "aarav", "diya", "riya", "kabir", "aarav", "meera");
        Set<String> uniqueUsers = new HashSet<>(logins);
        Set<String> firstSeenOrder = new LinkedHashSet<>(logins);

        System.out.println("Unique visitor count: " + uniqueUsers.size());
        System.out.println("First three distinct visitors:");
        firstSeenOrder.stream().limit(3).forEach(System.out::println);
    }
}
