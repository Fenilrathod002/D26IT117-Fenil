package model.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** Limits the number of characters allowed in a String field. */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface MaxLength {
    int value();
}
