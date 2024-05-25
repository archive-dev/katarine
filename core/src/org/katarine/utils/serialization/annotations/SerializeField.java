package org.katarine.utils.serialization.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface SerializeField {
    enum SerializationType {

        /**
         * Forces field to be serialized with {@code field.toString()} method
         */
        SIMPLE,

        /**
         * Forces field to be serialized with current serializer's {@code toString()} method
         */
        FULL
    }

    String fieldName() default "";

    SerializationType type() default SerializationType.FULL;
}
