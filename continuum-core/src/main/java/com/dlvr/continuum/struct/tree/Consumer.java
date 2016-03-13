package com.dlvr.continuum.struct.tree;

/**
 * Created by zack on 3/13/16.
 */

@FunctionalInterface
public interface Consumer<V> {
    boolean visit(int level, Tree<V> tree);
}
