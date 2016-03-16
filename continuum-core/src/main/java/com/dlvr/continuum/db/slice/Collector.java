package com.dlvr.continuum.db.slice;

/**
 * Time scan atom collection and analyzation
 * Created by zack on 2/11/16.
 */
public interface Collector<T> {
    String name();
    void collect(T atom);
    Slice slice();
}
