package com.dlvr.continuum.collect;

import org.junit.Test;

import java.util.Arrays;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * Created by zack on 3/11/16.
 */
public class TreeTest {
    @Test
    public void testSimple() {
        Tree<Double> tree = new Tree<>();

        tree.put("foo", 2.3);
        tree.put("one.two.three", 54.1);
        tree.put("one.two.to.the.foo", 54.1);
        tree.put("one.two.four", 54.1);

        assertEquals(4, tree.size());
        Set<String> keySet = tree.keySet();
        assertEquals(4, keySet.size());
    }
}