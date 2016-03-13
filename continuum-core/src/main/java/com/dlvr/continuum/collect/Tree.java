package com.dlvr.continuum.collect;


import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Directed, Acyclic graph
 *
 * Created by zack on 3/11/16.
 */
public class Tree<V> implements Visitable<V> {

    private final Tree<V> parent;

    private final Set<Tree<V>> nodes = new LinkedHashSet<>();

    public V data;

    public Tree(V data) {
        this(null, data);
    }

    public Tree(Tree<V> parent, V data) {
        this.parent = parent;
        this.data = data;
    }

    public Tree<V> child(V data) {
        for (Tree<V> child : nodes) {
            if (child.data.equals(data)) {
                return child;
            }
        }

        return child(new Tree<>(this, data));
    }

    public Tree<V> child(Tree<V> child) {
        nodes.add(child);
        return child;
    }

    public Tree<V> parent() {
        return parent;
    }

    public Set<Tree<V>> children() {
        return nodes;
    }

    @Override
    public String toString() {
        String s = "";
        if (nodes != null) s += nodes.toString();
        return s;
    }

    public void breadthFirst(Consumer<V> consumer) {
        accept(new BreadthFirst(consumer));
    }

    public void accept(Visitor<V> visitor) {
        visitor.visitData(this, data);
        for (Tree<V> child : children()) {
            Visitor<V> childVisitor = visitor.visitTree(child);
            if (childVisitor != null) // stop?
                child.accept(childVisitor);
        }
    }

    static class IndentVisitor<V> implements Visitor<V> {

        private final int indent;

        IndentVisitor(int indent) {
            this.indent = indent;
        }

        @Override
        public Visitor<V> visitTree(Tree<V> tree) {
            return new IndentVisitor<>(indent + 2);
        }

        @Override
        public void visitData(Tree<V> parent, V data) {
            for (int i = 0; i < indent; i++) {
                System.out.print(" ");
            }

            System.out.println(data);
        }
    }

    public class BreadthFirst implements Visitor<V> {

        private final Consumer<V> consumer;
        private int level;

        public BreadthFirst(Consumer<V> consumer) {
            this(0, consumer);
        }

        public BreadthFirst(int level, Consumer<V> consumer) {
            this.level = level;
            this.consumer = consumer;
        }

        @Override
        public Visitor<V> visitTree(Tree<V> tree) {
            boolean cont = consumer.visit(level, tree);
            return cont ? new BreadthFirst(level + 1, consumer) : null;
        }
        @Override
        public void visitData(Tree<V> parent, V data) {
            consumer.visit(level, parent);
        }
    }

    @FunctionalInterface
    public interface Consumer<V> {
        boolean visit(int level, Tree<V> tree);
    }
}
