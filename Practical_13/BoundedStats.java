public class BoundedStats {
    public static <N extends Number> double average(N[] values) {
        if (values == null || values.length == 0) {
            throw new IllegalArgumentException("Values must not be empty.");
        }
        double sum = 0.0;
        for (N value : values) {
            if (value == null) {
                throw new IllegalArgumentException("Values must not contain null.");
            }
            sum += value.doubleValue();
        }
        return sum / values.length;
    }

    public static void main(String[] args) {
        System.out.println("Integer average: " + average(new Integer[]{10, 20, 30}));
        System.out.println("Double average: " + average(new Double[]{2.5, 3.5, 4.0}));
        System.out.println("Long average: " + average(new Long[]{100L, 200L, 300L}));
        System.out.println("The <N extends Number> bound rejects non-number types at compile time.");
    }
}
