package com.dlvr.continuum.datastructure;

import org.junit.Test;

/**
 * Created by zack on 3/11/16.
 */
public class TreeTest {
    @Test
    public void testSimple() {
        Tree<Double> tree = new Tree<>(1.0);

        /*
        tree.ch("foo", 2.3);
        tree.put("one.two.three", 54.1);
        tree.put("one.two.to.the.foo", 54.1);
        tree.put("one.two.to.the.bar", Double.MAX_VALUE);
        tree.put("one.two.four", 54.1);

        assertEquals(5, tree.size());
        Set<String> keySet = tree.keySet();
        assertEquals(5, keySet.size());
        assertEquals(5, tree.values().size());
        assertEquals(3, new HashSet<>(tree.values()).size());

        assertNull(tree.get("ooon"));
        assertNull(tree.get("one.two"));
        assertNull(tree.get("one.two.tttt"));
        assertNull(tree.get("one.two.threee"));
        assertNull(tree.get("one.two.to.threee"));
        assertNull(tree.get("one.two.to.the"));
        assertEquals(54.1, tree.get("one.two.to.the.foo"), 0.0);
        assertEquals(Double.MAX_VALUE, tree.get("one.two.to.the.bar"), 0.0);
        */
    }
}