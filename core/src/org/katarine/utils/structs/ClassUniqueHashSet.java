package org.katarine.utils.structs;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.function.Predicate;

@SuppressWarnings("unchecked")
public class ClassUniqueHashSet<T> extends HashSet<T> implements ClassUnique<T> {
    private final HashMap<Class<? extends T>, T> classes = new HashMap<>();

    @Override
    public boolean hasClass(Class<? extends T> clazz) {
        return getClasses().containsKey(clazz);
    }

    @Override
    public boolean add(T t) {
        Class<? extends T> clazz;
        checkClass(clazz = (Class<? extends T>) t.getClass());
        getClasses().put(clazz, t);
        return super.add(t);
    }

    @Override
    public boolean remove(Object o) {
        Class<? extends T> clazz;
        checkClass(clazz = (Class<? extends T>) o.getClass());
        getClasses().remove(clazz);
        return super.remove(o);
    }

    @Override
    public void clear() {
        getClasses().clear();
        super.clear();
    }

    @Override
    public boolean retainAll(@NotNull Collection<?> c) {
        getClasses().keySet().retainAll(c.stream().toList());
        return super.retainAll(c);
    }


    @Deprecated
    @Override
    public boolean removeIf(Predicate<? super T> filter) {
        return super.removeIf(filter);
    }


    @Override
    public boolean removeAll(Collection<?> c) {
        c.stream().map(Object::getClass).toList().forEach(getClasses().keySet()::remove);
        return super.removeAll(c);
    }

    public T getByClass(Class<? extends T> clazz) {
        T ret = getClasses().get(clazz);
        if (ret != null) return ret;
        return getClasses().get(getClasses().keySet().stream().filter(clazz::isAssignableFrom).findFirst().orElse(null));
    }

    public T getBySuperclass(Class<?> clazz) {
        T ret = getClasses().get(clazz);
        if (ret!=null) return ret;

        for (Class<? extends T> v : getClasses().keySet()) {
            if (clazz.isAssignableFrom(v)) return getClasses().get(v);
        }
        return null;
    }

    public T getByExactClass(Class<? extends T> clazz) {
        return getClasses().get(clazz);
    }

    protected Map<Class<? extends T>, T> getClasses() {
        return classes;
    }
}
