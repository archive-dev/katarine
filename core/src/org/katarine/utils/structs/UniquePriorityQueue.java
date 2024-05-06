package org.katarine.utils.structs;

import java.util.Collection;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.SortedSet;

public class UniquePriorityQueue<E> extends PriorityQueue<E> {
    public UniquePriorityQueue() {
        super();
    }

    public UniquePriorityQueue(int initialCapacity) {
        super(initialCapacity);
    }

    public UniquePriorityQueue(Comparator<? super E> comparator) {
        super(comparator);
    }

    public UniquePriorityQueue(int initialCapacity, Comparator<? super E> comparator) {
        super(initialCapacity, comparator);
    }

    public UniquePriorityQueue(Collection<? extends E> c) {
        super(c);
    }

    public UniquePriorityQueue(PriorityQueue<? extends E> c) {
        super(c);
    }

    public UniquePriorityQueue(SortedSet<? extends E> c) {
        super(c);
    }

    @Override
    public boolean add(E e) {
        if (contains(e)) return false;
        return super.add(e);
    }

    @Override
    public boolean offer(E e) {
        if (contains(e)) return false;
        return super.offer(e);
    }
}
