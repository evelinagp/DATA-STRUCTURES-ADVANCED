package main;

import java.util.*;
import java.util.stream.Collectors;

public class Hierarchy<T> implements IHierarchy<T> {

    private Map<T, HierarchyNode<T>> data;
    private HierarchyNode<T> root;

    public Hierarchy(T element) {
        this.data = new HashMap<>();


        HierarchyNode<T> root = new HierarchyNode<>(element);
        this.root = root;
        this.data.put(element, root);
    }

    @Override
    public int getCount() {
        return this.data.size();
    }

    @Override
    public void add(T element, T child) {

        HierarchyNode<T> parent = ensureExistsAndGet(element);

        if (data.containsKey(child)) {
            throw new IllegalArgumentException();
        }
        HierarchyNode<T> toBeAdded = new HierarchyNode<>(child);
        toBeAdded.setParent(parent);
        parent.getChildren().add(toBeAdded);

        this.data.put(child, toBeAdded);
        this.data.put(parent.getValue(), parent);


    }

    @Override
    public void remove(T element) {
        HierarchyNode<T> toBeRemoved = ensureExistsAndGet(element);
        if (toBeRemoved.getParent() == null) {
            throw new IllegalStateException();
        }

        HierarchyNode<T> parent = toBeRemoved.getParent();
        List<HierarchyNode<T>> children = toBeRemoved.getChildren();
        children.forEach(c -> c.setParent(parent));

        parent.getChildren().addAll(children);
        parent.getChildren().remove(toBeRemoved);

        this.data.remove(toBeRemoved.getValue());
    }

    private HierarchyNode<T> ensureExistsAndGet(T key) {
        HierarchyNode<T> element = this.data.get(key);
        if (element == null) {
            throw new IllegalArgumentException();
        }
        return element;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Iterable<T> getChildren(T element) {
        HierarchyNode<T> current = ensureExistsAndGet(element);

        return current.getChildren().stream().map(HierarchyNode::getValue).collect(Collectors.toList());
    }

    @Override
    public T getParent(T element) {
        HierarchyNode<T> current = ensureExistsAndGet(element);
        return current.getParent() == null ? null : current.getParent().getValue();
    }

    @Override
    public boolean contains(T element) {
        return this.data.containsKey(element);
    }

    @Override
    public Iterable<T> getCommonElements(IHierarchy<T> other) {
//        Queue<T> result = new PriorityQueue<>();
        List<T> result = new ArrayList<>();
        this.data.keySet().forEach(k -> {
            if (other.contains(k)) {
                result.add(k);
            }
        });

        return result;
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            Deque<HierarchyNode<T>> deque = new ArrayDeque<>(Collections.singletonList(root));

            @Override
            public boolean hasNext() {
                return deque.size() > 0;
            }

            @Override
            public T next() {
                HierarchyNode<T> nextElement = deque.poll();
                deque.addAll(nextElement.getChildren());
                return nextElement.getValue();
            }
        };
    }
}
