import java.lang.annotation.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

// Custom annotation to reject blank values
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@interface NotBlank {
}

// Custom annotation to limit text length
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@interface MaxLength {
    int value();
}

// Signup form
class SignupForm {
    @NotBlank
    @MaxLength(20)
    String username;

    @NotBlank
    @MaxLength(30)
    String email;

    SignupForm(String username, String email) {
        this.username = username;
        this.email = email;
    }
}

// Validator using reflection
public class FormValidator {

    public static List<String> validate(Object obj)
            throws IllegalAccessException {

        List<String> errors = new ArrayList<>();

        for (Field field : obj.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            Object value = field.get(obj);

            if (field.isAnnotationPresent(NotBlank.class)) {
                if (value == null ||
                    value.toString().trim().isEmpty()) {
                    errors.add(field.getName()
                            + " must not be blank");
                }
            }

            if (field.isAnnotationPresent(MaxLength.class)
                    && value != null) {

                MaxLength annotation =
                        field.getAnnotation(MaxLength.class);

                if (value.toString().length()
                        > annotation.value()) {
                    errors.add(field.getName()
                            + " exceeds maximum length of "
                            + annotation.value());
                }
            }
        }

        return errors;
    }

    public static void main(String[] args)
            throws IllegalAccessException {

        SignupForm form =
                new SignupForm("", "this_is_a_very_long_email_address"
                        + "_that_exceeds_thirty_characters");

        List<String> errors = validate(form);

        if (errors.isEmpty()) {
            System.out.println("Form is valid!");
        } else {
            System.out.println("Validation Errors:");
            for (String error : errors) {
                System.out.println(error);
            }
        }
    }
}
