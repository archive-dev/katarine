package org.katarine.core;

import org.katarine.logging.LogLevel;
import org.katarine.logging.Logger;
import org.katarine.utils.Utils;
import org.katarine.utils.serialization.Serializable;
import org.katarine.utils.serialization.annotations.DontSerialize;

import java.util.HashMap;

// KObject is a base class for GameObject, Component and other katarine objects
public interface KObject extends Serializable {
    @Override
    default HashMap<String, Object> getFields() {
        try {
            return getFieldsRecursively(this.getClass(), null, this);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    default void fillFields(HashMap<String, Object> fields) {
        try {
            for (var f : this.getClass().getDeclaredFields()) {
                    f.set(this, fields.get(f.getName()));
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private static HashMap<String, Object> getFieldsRecursively(Class<?> clazz, HashMap<String, Object> currentFields, Object obj) throws IllegalAccessException {
        var l = new Logger(LogLevel.INFO);
        l.debug(clazz);

        if (currentFields==null) {
            currentFields = new HashMap<>();
        }

        if (clazz.isAnnotationPresent(DontSerialize.class)) return getFieldsRecursively(clazz.getSuperclass(), currentFields, obj);
        for (var f : clazz.getDeclaredFields()) {
            if (f.isAnnotationPresent(DontSerialize.class)) continue;
            l.debug(clazz.getSimpleName() + "; " + f.getName());
            if (f.getType().isArray()) {
                currentFields.put(f.getName(), new ArrayWrapper<>(((Object[]) Utils.getFieldValue(f, obj))));
            }
            else currentFields.put(f.getName(), Utils.getFieldValue(f, obj));
        }
        if (clazz.equals(Object.class)) return currentFields;
        return getFieldsRecursively(clazz.getSuperclass(), currentFields, obj);
    }
}

class ArrayWrapper<T> implements Serializable {
    public ArrayWrapper() {}

    T[] array;
    ArrayWrapper(T[] array) {
        this.array = array;
    }

    @Override
    public HashMap<String, Object> getFields() {
        final HashMap<String, Object> fields = new HashMap<>();

        int c = 0;
        for (var v : this.array) {
            fields.put("i"+c, v);
            c++;
        }

        return fields;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void fillFields(HashMap<String, Object> fields) {
        T[] arr = (T[]) new Object[fields.size()];

        for (var f : fields.entrySet()) {
            arr[Integer.parseInt(f.getKey().substring(1))] = (T) f.getValue();
        }
    }
}