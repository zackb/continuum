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

    @Override
    public String toString() {
        String s = "";
        if (nodes != null) s += nodes.toString();
        return s;
    }

    public void accept(Visitor<V> visitor) {
        visitor.visitData(this, data);
        for (Tree<V> child : nodes) {
            Visitor<V> childVisitor = visitor.visitTree(child);
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
}
