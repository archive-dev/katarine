package org.katarine.utils.structs.tree;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Node<T> {
    protected T data;

    protected Node<T> parent;
    protected final ArrayList<Node<T>> children = new ArrayList<>();

    public Node(T data) {
        this.data = data;
    }

    public Node() {
        this(null);
    }

    /**
     * Adds a child node to the current node.
     *
     * @param child the child node to be added
     * @return the added child node
     */
    public Node<T> addChild(Node<T> child) {
        child.parent = this;
        children.add(child);
        return child;
    }

    public Node<T> addChild(T child) {
        return addChild(new Node<>(child));
    }

    public void addAllNodes(Collection<Node<T>> children) {
        this.children.addAll(children);
    }

    public void addAllChildren(Collection<T> children) {
        addAllNodes(children.stream().map(Node::new).toList());
    }

    /**
     * Removes a child from the current Node.
     * @param child Child to remove.
     * @return Removed child.
     */
    public Node<T> removeChild(Node<T> child) {
        this.children.remove(child);
        return child;
    }

    public Node<T> removeChildAt(int index) {
        return removeChild(this.children.get(index));
    }

    /**
     * Removes an element from this tree.
     * When doing this or something similar:
     * <pre>
     *     Node tree = new Node<>();
     *     tree.removeElement(tree);
     * </pre>
     * Then {@code tree.data} is set to {@code null}:
     * <pre>
     *     tree.data == null; // true
     * </pre>
     * @param element Element to remove.
     * @return Removed element
     */
    public Node<T> removeElement(Node<T> element) {
        if (element.getParent()==null) this.data = null;
        element.getParent().getChildren().remove(element);
        return element;
    }

    public List<Node<T>> getChildren() {
        return this.children;
    }

    public Node<T> getParent() {
        return this.parent;
    }

    public T get() {
        return this.data;
    }
    public void set(T data) {
        this.data = data;
    }
}
