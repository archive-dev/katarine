package org.katarine.utils.structs;

import java.util.*;

public class UniqueClassPriorityQueue<E> extends UniquePriorityQueue<E> {
    HashSet<Class<?>> classes = new HashSet<>();

    public UniqueClassPriorityQueue() {
        super();
    }

    public UniqueClassPriorityQueue(int initialCapacity) {
        super(initialCapacity);
    }

    public UniqueClassPriorityQueue(Comparator<? super E> comparator) {
        super(comparator);
    }

    public UniqueClassPriorityQueue(int initialCapacity, Comparator<? super E> comparator) {
        super(initialCapacity, comparator);
    }

    public UniqueClassPriorityQueue(Collection<? extends E> c) {
        super(c);
    }

    public UniqueClassPriorityQueue(PriorityQueue<? extends E> c) {
        super(c);
    }

    public UniqueClassPriorityQueue(SortedSet<? extends E> c) {
        super(c);
    }

    @Override
    public boolean add(E e) {
        if (classes.contains(e.getClass())) return false;
        return super.add(e);
    }

    @Override
    public boolean offer(E e) {
        if (classes.contains(e.getClass())) return false;
        return super.offer(e);
    }
}
