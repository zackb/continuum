package com.dlvr.continuum.collect;


import com.dlvr.continuum.util.Strings;

import java.util.*;

/**
 * Recursive group and sub-group Map datastructure.
 * Uses DELIM to nest recursive sub-groups
 *
 * Created by zack on 3/11/16.
 */
public class Tree<V> implements Map<String, V> {

    private static final char DELIM = '.';

    private Map<String, V> node;

    private Map<String, Tree<V>> nodes;

    @Override
    public int size() {
        return keySet().size();
    }

    @Override
    public boolean isEmpty() {
        return (node == null || nodes.isEmpty())
                &&
               (nodes == null || nodes.isEmpty());
    }

    @Override
    public boolean containsKey(Object key) {
        return (node != null && nodes.containsKey(key))
                ||
               (nodes != null && nodes.containsKey(key));
    }

    @Override
    public boolean containsValue(Object value) {
        return
                (node != null && node.containsValue(value))
                ||
                (nodes != null && nodes.containsValue(value));
    }

    @Override
    public V get(Object key) {
        String[] parts = k(key);
        if (parts.length == 1 && node != null)
            return node.get(key);

        if (nodes != null) {
            Tree<V> sub = nodes.get(parts[0]);
            if (sub != null) return sub.get(subkey((String)key));
        }
        return null;
    }

    @Override
    public V put(String key, V value) {
        String[] parts = k(key);
        V res = value;
        if (parts.length == 1) {
            if (node == null) node = new HashMap<>();
            res = node.put(key, value);
        } else {
            if (nodes == null) nodes = new HashMap<>();
            if (nodes.get(parts[0]) == null) {
                nodes.put(parts[0], new Tree<V>());
            }
            res = nodes.get(parts[0]).put(subkey(key), value);
        }
        return res;
    }

    @Override
    public V remove(Object key) {
        return put((String)key, null);
    }

    @Override
    public void putAll(Map<? extends String, ? extends V> m) {
        m.forEach(this::put);
    }

    @Override
    public void clear() {
        if (node != null) node.clear();
        node = null;
        if (nodes != null) nodes.clear();
        nodes = null;
    }

    @Override
    public Set<String> keySet() {
        Set<String> ks = new HashSet<>();

        if (node != null) ks.addAll(node.keySet());
        if (nodes != null) nodes.keySet().forEach(s -> nodes.get(s).add(s, ks));
        return ks;
    }

    private void add(String prefix, Set<String> keyset) {
        if (nodes != null) nodes.keySet().forEach(s -> nodes.get(s).add(prefix + DELIM + s, keyset));
        if (node != null) node.keySet().forEach(s -> keyset.add(prefix + DELIM + s));
    }

    @Override
    public Collection<V> values() {
        Collection<V> vals = new ArrayList<>();
        if (node != null) vals.addAll(node.values());

        if (nodes != null) nodes.keySet().forEach(s -> vals.addAll(nodes.get(s).values()));

        return vals;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Set<Map.Entry<String, V>> entrySet() {
        if (nodes == null && node == null) return null;
        Set<Map.Entry<String, V>> es = new HashSet<>();
        if (node != null) es.addAll(node.entrySet());
        if (nodes != null) nodes.keySet().forEach(s -> es.addAll(nodes.get(s).entrySet()));
        return es;
    }

    private String subkey(String keys) {
        return subkey(k(keys));
    }

    private String subkey(String[] keys) {
        return String.join("" + DELIM, Arrays.asList(keys).subList(1, keys.length));
    }

    private String[] k(String key) {
        return key.split("\\" + DELIM);
    }

    private String[] k(Object key) {
        return ((String)key).split("\\" + DELIM);
    }

    @Override
    public String toString() {
        String s = "";
        if (node != null) s += node.toString();
        if (nodes != null) s += nodes.toString();
        return s;
    }

    public void each(TreeConsumer<V> consumer) {
        keySet().stream()
                .sorted((s1, s2) -> s1.length() > s2.length() ? 1 : -1)
                .forEach(s ->
                    consumer.apply(s, get(s))
                );
    }

    public void search(TreeSearch<V> consumer) {
        search(consumer, "");
    }

    private void search(TreeSearch<V> consumer, String prefix) {

        if (nodes != null)
            nodes.keySet().forEach(s -> nodes.get(s).search(consumer, prefix + (Strings.empty(prefix) ? "" : DELIM) + s));

        if (node != null)
            node.forEach((s, v) -> consumer.apply(this, prefix + (Strings.empty(prefix) ? "" : DELIM) + s, v));
    }

    @FunctionalInterface
    interface TreeConsumer<V> {
        void apply(String s, V v);
    }

    @FunctionalInterface
    interface TreeSearch<V> {
        void apply(Tree<V> subtree, String s, V v);
    }

    public static void main(String[] args) {
        Tree<Double> tree = new Tree<>();

        tree.put("foo", 1.0);
        tree.put("foo.bar", 2.0);
        tree.put("foo.bar", 3.0);
        tree.put("foo.baz", 5.0);
        tree.put("bad.boy", 56.0);
        tree.put("one.to.the.two", 102.0);
        tree.put("one.to.the.three", 103.0);
        tree.put("one.to.the.four", 104.0);
        tree.search((t, s, d) -> System.out.println(s + ": " + d));
    }
}
