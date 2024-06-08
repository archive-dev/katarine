package org.katarine.utils.structs;

import java.util.*;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

@SuppressWarnings("unchecked")
public class ClassUniqueArrayList<T> extends ArrayList<T> implements ClassUnique<T> {
    private final HashSet<Class<? extends T>> classes = new HashSet<>();

    @Override
    public boolean hasClass(Class<? extends T> clazz) {
        return classes.contains(clazz);
    }

    @Override
    public boolean add(T t) {
        return super.add(t);
    }

    @Override
    public void add(int index, T element) {
        Class<? extends T> clazz;
        checkClass(clazz = (Class<? extends T>) element.getClass());
        super.add(index, element);
        this.classes.add(clazz);
    }

    @Override
    public T set(int index, T element) {
        Class<? extends T> clazz;
        checkClass(clazz = (Class<? extends T>) element.getClass());
        T ret = super.set(index, element);
        if (ret!=null)
            this.classes.remove(clazz);
        this.classes.add(clazz);
        return ret;
    }

    @Override
    public T remove(int index) {
        T ret = super.remove(index);
        classes.remove(ret.getClass());
        return ret;
    }

    @Override
    public void clear() {
        classes.clear();
        super.clear();
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        c.forEach(el -> {
            checkClass((Class<? extends T>) el.getClass());
            this.classes.add((Class<? extends T>) el.getClass());
        });
        return super.addAll(c);
    }

    @Override
    public boolean addAll(int index, Collection<? extends T> c) {
        c.forEach(el -> {
            checkClass((Class<? extends T>) el.getClass());
            this.classes.add((Class<? extends T>) el.getClass());
        });
        return super.addAll(index, c);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        c.forEach(el -> classes.remove(el.getClass()));
        return super.removeAll(c);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        classes.retainAll(c.stream().map(Object::getClass).toList());
        return super.retainAll(c);
    }

    @Override
    protected void removeRange(int fromIndex, int toIndex) {
        for (int i = fromIndex; i < toIndex; i++) {
            classes.remove(get(i).getClass());
        }
        super.removeRange(fromIndex, toIndex);
    }

    /**
     *
     * @param filter a predicate which returns {@code true} for elements to be
     *        removed
     * @return True, if any element was removed, otherwise returns false.
     *
     * @deprecated I couldn't think of a way to implement this, so please do not use it. Or, implement this method and send the PR to GitHub repo.
     */
    @Deprecated()
    @Override
    public boolean removeIf(Predicate<? super T> filter) {

        return super.removeIf(filter);
    }

    /**
     *
     * @param operator the operator to apply to each element
     *
     * @deprecated I couldn't think of a way to implement this, so please do not use it. Or, implement this method and send the PR to GitHub repo.
     */
    @Deprecated
    @Override
    public void replaceAll(UnaryOperator<T> operator) {

        super.replaceAll(operator);
    }
}
