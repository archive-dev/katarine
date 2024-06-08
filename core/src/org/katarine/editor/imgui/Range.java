package org.katarine.editor.imgui;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public final class Range {
    private Range(){}

    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface FloatRange {
        float min() default Float.MIN_VALUE;
        float max() default Float.MAX_VALUE;
    }

    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface IntegerRange {
        int min() default Integer.MIN_VALUE;
        int max() default Integer.MAX_VALUE;
    }


    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface DoubleRange {
        double min() default Double.MIN_VALUE;
        double max() default Double.MAX_VALUE;
    }

    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface LongRange {
        long min() default Long.MIN_VALUE;
        long max() default Long.MAX_VALUE;
    }
}
