package com.dlvr.continuum.collect;


import java.util.*;

/**
 * Recursive group and sub-group Map datastructure.
 * Uses DELIM to nest recursive sub-groups
 *
 * Created by zack on 3/11/16.
 */
public class Tree<V> {//implements Map<String, V> {

    private static final String DELIM = "\\.";

    private Map<String, V> node;

    private Tree<V> nodes;

    //@Override
    public int size() {
        int size = 0;
        if (node != null) size += node.size();
        if (nodes != null) size += nodes.size();
        return size;
    }

    //@Override
    public boolean isEmpty() {
        return (node == null || nodes.isEmpty())
                &&
               (nodes == null || nodes.isEmpty());
    }

    //@Override
    public boolean containsKey(Object key) {
        return (node != null && nodes.containsKey(key))
                ||
               (nodes != null && nodes.containsKey(key));
    }

    //@Override
    public boolean containsValue(Object value) {
        return
                (node != null && node.containsValue(value))
                ||
                (nodes != null && nodes.containsValue(value));
    }

    //@Override
    public V get(Object key) {
        String[] parts = k(key);
        return parts.length == 1 && node != null ? node.get(key) : nodes == null ? null : nodes.get(subkey(parts));
    }

    //@Override
    public V put(String key, V value) {
        String[] parts = k(key);
        V res = value;
        if (parts.length == 1) {
            if (node == null) node = new HashMap<>();
            res = node.put(key, value);
        } else {
            if (nodes == null) nodes = new Tree<>();
            res = nodes.put(subkey(key), value);
        }
        return res;
    }

    //@Override
    public V remove(Object key) {

        return null;
    }

    //@Override
    public void putAll(Map<? extends String, ? extends V> m) {
        m.forEach(this::put);
    }

    //@Override
    public void clear() {
        if (node != null) node.clear();
        node = null;
        if (nodes != null) nodes.clear();
        nodes = null;
    }

    //@Override
    public Set<String> keySet() {
        Set<String> ks = new HashSet<>();

        if (node != null) {
            if (nodes != null) {
                node.keySet().forEach(s -> {
                    nodes.keySet().forEach(k -> {
                        ks.add(s + "." + k);
                    });
                });
            } else {
                ks.addAll(node.keySet());
            }
        } else if (nodes != null) {
            ks.addAll(nodes.keySet());
        }

        return ks;
    }
    /*

    private void add(String prefix, Set<String> keyset) {
        if (nodes != null) nodes.keySet().forEach(s -> keyset.add(prefix + "." + s));
        if (node != null) node.keySet().forEach(s -> keyset.add(prefix + "." + s));
    }
    */

    //@Override
    public Collection<V> values() {
        Collection<V> vals = new ArrayList<>();
        if (node != null) vals.addAll(node.values());

        if (nodes != null) vals.addAll(nodes.values());

        return vals;
    }

    //@Override
    @SuppressWarnings("unchecked")
    public Set<Map.Entry<String, V>> entrySet() {
        if (nodes == null && node == null) return null;
        Set<Map.Entry<String, V>> es = new HashSet<>();
        if (node != null) es.addAll(node.entrySet());
        if (nodes != null) es.addAll(nodes.entrySet());
        return es;
    }

    private String subkey(String keys) {
        return subkey(k(keys));
    }

    private String subkey(String[] keys) {
        return String.join(".", Arrays.asList(keys).subList(1, keys.length));
    }

    private String[] k(String key) {
        return key.split(DELIM);
    }

    private String[] k(Object key) {
        return ((String)key).split(DELIM);
    }

    @Override
    public String toString() {
        String s = "";
        if (node != null) s += node.toString();
        if (nodes != null) s += nodes.toString();
        return s;
    }

    public static void main(String[] args) {
        Tree<Double> tree = new Tree<>();

        tree.put("foo", 1.0);
        //tree.put("foo.bar", 2.0);
        //tree.put("foo.bar", 3.0);
        //tree.put("foo.baz", 5.0);
        //tree.put("bad.boy", 56.0);
        tree.put("one.to.the.two", 102.0);
        System.out.println(tree.get("one.to.the.two"));
        System.out.println(tree.get("foo.bar"));
        System.out.println(tree.get("bad"));
        System.out.println(tree.get("bad.boy"));
        System.out.println(tree.keySet());
    }
}
