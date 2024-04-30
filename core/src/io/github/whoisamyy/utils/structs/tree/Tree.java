package io.github.whoisamyy.utils.structs.tree;

import javax.annotation.Nonnull;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

/**
 * N-ary tree implementation.
 * @param <T>
 */
public class Tree<T> extends Node<T> implements Iterable<T> {
    public Tree(T data) {
        super(data);
    }

    public Tree() {
        super();
    }

    @Override
    @Nonnull
    public Iterator<T> iterator() {
        return new TreeIterator();
    }

    public class TreeIterator implements Iterator<T> {
        final private Queue<Node<T>> queue = new LinkedList<>();
        {
            queue.add(Tree.this);
        }

        @Override
        public boolean hasNext() {
            return !queue.isEmpty();
        }

        @Override
        public T next() {
            Node<T> current = queue.poll();
            queue.addAll(current.children);
            return current.get();
        }
    }
}
