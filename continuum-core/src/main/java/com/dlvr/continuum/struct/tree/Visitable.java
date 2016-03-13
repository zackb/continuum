package com.dlvr.continuum.struct.tree;

/**
 * Visitable
 * Created by zack on 3/12/16.
 */
public interface Visitable<V> {
    void accept(Visitor<V> visitor);
}
