package io.github.whoisamyy.utils;

import java.lang.reflect.Field;

public class Utils {
    public static void setStaticFieldValue(Class<?> clazz, String fieldName, Object fieldValue) throws NoSuchFieldException, IllegalAccessException {
        Field field = clazz.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(null, fieldValue);
        field.setAccessible(false);
    }

    public static void setFieldValue(Object objectInstance, String fieldName, Object fieldValue) throws NoSuchFieldException, IllegalAccessException {
        Field field = objectInstance.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(objectInstance, fieldValue);
        field.setAccessible(false);
    }
}
