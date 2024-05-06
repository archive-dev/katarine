package org.katarine.core;

import com.badlogic.gdx.ApplicationAdapter;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Objects;

public class Window extends ApplicationAdapter implements KObject {
    private String name;

    protected Window() {}

    public static Window create(String name) {
        return new Window();
    }

    public static Window create(Class<? extends Window> clazz) {
        Constructor<? extends Window> constr = null;
        boolean canAccess = false;
        boolean er = false;
        try {
            constr = clazz.getDeclaredConstructor();
            canAccess = constr.canAccess(null);
            constr.setAccessible(true);
            return constr.newInstance();
        } catch (Exception e) {
            er = true;
            throw new RuntimeException(e);
        } finally {
            if (!er)
                Objects.requireNonNull(constr).setAccessible(canAccess);
        }
    }

    public static Window create(Class<? extends Window> clazz, Object... args) {
        Constructor<? extends Window> constr = null;
        boolean canAccess = false;
        boolean er = false;
        try {
            constr = clazz.getDeclaredConstructor(Arrays.stream(args).map(Object::getClass).toArray(Class[]::new));
            canAccess = constr.canAccess(null);
            constr.setAccessible(true);
            return constr.newInstance(args);
        } catch (InvocationTargetException | NoSuchMethodException | InstantiationException | IllegalAccessException e) {
            er = true;
            return create(clazz);
        } finally {
            if (!er)
                Objects.requireNonNull(constr).setAccessible(canAccess);
        }
    }
}
