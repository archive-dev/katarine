package org.katarine.utils.structs.concurrent;

import org.jetbrains.annotations.NotNull;
import org.katarine.utils.structs.ClassUnique;
import org.katarine.utils.structs.ClassUniqueHashSet;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class ConcurrentClassUniqueHashSet<T> extends ClassUniqueHashSet<T> implements ClassUnique<T> {
    private final ConcurrentHashMap<Class<? extends T>, T> classes = new ConcurrentHashMap<>();

    private final Object lock = new Object(); // объект для синхронизации

    @Override
    public boolean hasClass(Class<? extends T> clazz) {
        synchronized (lock) {
//            new Logger(LogLevel.DEBUG).debug(clazz + " " + classes.containsKey(clazz));
            return classes.containsKey(clazz);
        }
    }

    @Override
    public boolean add(T t) {
//        if (hasClass((Class<? extends T>) t.getClass())) {
//            for (var f : Thread.currentThread().getStackTrace()) {
//                System.out.println(f);
//            }
//        }
        synchronized (lock) {
//            Class<? extends T> clazz;
//            checkClass(clazz = (Class<? extends T>) t.getClass());
//            classes.put(clazz, t);
            return super.add(t);
        }
    }

    @Override
    public boolean remove(Object o) {
        synchronized (lock) {
//            Class<? extends T> clazz;
//            checkClass(clazz = (Class<? extends T>) o.getClass());
//            classes.remove(clazz);
            return super.remove(o);
        }
    }

    @Override
    public void clear() {
        synchronized (lock) {
//            classes.clear();
            super.clear();
        }
    }

    @Override
    public boolean addAll(@NotNull Collection<? extends T> c) {
        synchronized (lock) {
            return super.addAll(c);
        }
    }

    @Override
    public boolean retainAll(@NotNull Collection<?> c) {
        synchronized (lock) {
            return super.retainAll(c);
        }
    }

    @Deprecated
    @Override
    public boolean removeIf(Predicate<? super T> filter) {
        synchronized (lock) {
            return super.removeIf(filter);
        }
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        synchronized (lock) {
            return super.removeAll(c);
        }
    }

    @Override
    public T getByClass(Class<? extends T> clazz) {
        synchronized (lock) {
            T ret = classes.get(clazz);
            if (ret != null) return ret;

            for (Class<? extends T> v : classes.keySet()) {
                if (clazz.isAssignableFrom(v)) return classes.get(v);
            }
            return null;
        }
    }

    @Override
    public T getBySuperclass(Class<?> clazz) {
        synchronized (lock) {
            T ret = classes.get(clazz);
            if (ret!=null) return ret;

            for (Class<? extends T> v : classes.keySet()) {
                if (clazz.isAssignableFrom(v)) return classes.get(v);
            }
            return null;
        }
    }

    @Override
    public T getByExactClass(Class<? extends T> clazz) {
        synchronized (lock) {
            return classes.get(clazz);
        }
    }

    public void forEachSynchronized(Consumer<? super T> action) {
        synchronized (lock) {
            super.forEach(action);
        }
    }

    public void forEachSynchronized(long parallelismThreshold, Consumer<? super T> action) {
        classes.forEachValue(parallelismThreshold, action);
    }

    @Override
    protected Map<Class<? extends T>, T> getClasses() {
        return classes;
    }
}