package io.github.whoisamyy.utils;

import java.lang.reflect.Field;

public class Utils {
    /**
     * Pixels Per Meter.
     */
    public static final float PPM = 64f;
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

    public static Object getStaticFieldValue(Class<?> clazz, String fieldName) throws NoSuchFieldException, IllegalAccessException {
        Field field = clazz.getDeclaredField(fieldName);
        field.setAccessible(true);
        Object o = field.get(null);
        field.setAccessible(false);
        return o;
    }

    public static Object getFieldValue(Object objectInstance, String fieldName) throws NoSuchFieldException, IllegalAccessException {
        Field field = objectInstance.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        Object o = field.get(objectInstance);
        field.setAccessible(false);
        return o;
    }
}
