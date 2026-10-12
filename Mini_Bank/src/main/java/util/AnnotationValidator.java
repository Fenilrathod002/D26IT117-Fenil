package util;

import java.lang.reflect.*;
import java.util.*;
import model.annotation.*;

public final class AnnotationValidator {

    private AnnotationValidator() {
    }

    public static String[] validate(Object obj) {
        if (obj == null) {
            return new String[]{"object: must not be null"};
        
        }List<String> errors = new ArrayList<>();
        for (Field f : obj.getClass().getDeclaredFields()) {
            try {
                f.setAccessible(true);
                Object value = f.get(obj);
                if (f.isAnnotationPresent(Positive.class) && value instanceof Number n && n.doubleValue() <= 0) {
                    errors.add(f.getName() + ": " + f.getAnnotation(Positive.class).message());
                
                }if (f.isAnnotationPresent(MaxLength.class) && value instanceof String s && s.length() > f.getAnnotation(MaxLength.class).value()) {
                    errors.add(f.getName() + ": exceeds maximum length " + f.getAnnotation(MaxLength.class).value());
            
                }} catch (ReflectiveOperationException | SecurityException e) {
                errors.add(f.getName() + ": cannot inspect field");
            }
        }
        return errors.toArray(String[]::new);
    }
}
