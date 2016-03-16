package com.dlvr.continuum.db.slice;

/**
 * Time scan atom collection and analyzation
 * Created by zack on 2/11/16.
 */
public interface Collector<T> {

    /**
     * Name / label for this slice
     * @return name of collected slice
     */
    String name();

    /**
     * Collect an Atom or an AtomID
     * @param t atom or id
     */
    void collect(T t);

    /**
     * Get result slice
     * @return result slice
     */
    Slice slice();
}
