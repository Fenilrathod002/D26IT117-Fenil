import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;

// Custom annotation for test methods
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@interface Run {
}

// Class containing test and non-test methods
class MyTests {

    @Run
    public void testAddition() {
        System.out.println("Test Addition: "
                + (2 + 3 == 5));
    }

    @Run
    public void testSubtraction() {
        System.out.println("Test Subtraction: "
                + (5 - 2 == 3));
    }

    public void normalMethod() {
        System.out.println("This method should not run.");
    }

    @Run
    public void testMultiplication() {
        System.out.println("Test Multiplication: "
                + (3 * 4 == 12));
    }
}

// Mini test runner
public class MiniTestRunner {

    public static void main(String[] args)
            throws Exception {

        MyTests tests = new MyTests();
        int count = 0;

        for (Method method :
                tests.getClass().getDeclaredMethods()) {

            if (method.isAnnotationPresent(Run.class)) {
                method.invoke(tests);
                count++;
            }
        }

        System.out.println("Total tests run: " + count);
    }
}
