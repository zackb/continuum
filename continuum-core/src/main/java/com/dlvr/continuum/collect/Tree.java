package com.dlvr.continuum.collect;


import java.util.*;

/**
 * Directed, Acyclic graph
 *
 * Created by zack on 3/11/16.
 */
public class Tree<V> implements Visitable<V> {

    private static final String DELIM = ".";

    private final Tree<V> parent;

    private final Set<Tree<V>> nodes = new LinkedHashSet<>();

    private V data;

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

    Tree<V> child(Tree<V> child) {
        nodes.add(child);
        return child;
    }

    public Tree<V> parent() {
        return parent;
    }

    public void clear() {
        data = null;
        if (nodes != null) nodes.clear();
    }

    private String subkey(String keys) {
        return subkey(k(keys));
    }

    private String subkey(String[] keys) {
        return subkey(1, keys);
    }

    private String subkey(int start, String[] keys) {
        return String.join("" + DELIM, Arrays.asList(keys).subList(start, keys.length));
    }

    private String[] k(String key) {
        return key.split("\\" + DELIM);
    }

    private String[] k(Object key) {
        return ((String) key).split("\\" + DELIM);
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
    public static void main(String[] args) {
        Tree<String> tree = new Tree<>("/");

        tree.child("foo");
        tree.child("foo.bar");
        tree.child("foo.baz");
        tree.child("foo.bar.baz");
        tree.accept(new IndentVisitor<>(0));
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
            for (int i = 0; i < indent; i++) { // TODO: naive implementation
                System.out.print(" ");
            }

            System.out.println(data);
        }
    }
}
