public class GenericContainersDemo {
    public static <T> T first(T[] values) {
        if (values == null || values.length == 0) {
            throw new IllegalArgumentException("Array must contain at least one value.");
        }
        return values[0];
    }

    public static void main(String[] args) {
        Box<String> message = new Box<>("Generic types in Java");
        Box<Integer> count = new Box<>(3);
        Pair<String, Integer> accountSummary = new Pair<>("AC0001", 5000);

        System.out.println("String box: " + message.get());
        System.out.println("Integer box: " + count.get());
        System.out.println("Pair: " + accountSummary.first() + " -> " + accountSummary.second());
        System.out.println("First string: " + first(new String[]{"Java", "Generics"}));
        System.out.println("First integer: " + first(new Integer[]{10, 20, 30}));
    }
}
