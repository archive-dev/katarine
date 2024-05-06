package org.katarine.utils.serialization.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({java.lang.annotation.ElementType.FIELD})
@Retention(RetentionPolicy.CLASS)
public @interface DontSerialize {
    String fieldName() default "";
}
