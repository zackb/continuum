package com.dlvr.continuum.db.slice;

import com.dlvr.continuum.atom.Atom;
import com.dlvr.continuum.atom.Values;

import java.util.List;

/**
 * Slice of space time
 */
public interface Slice {
    String name();
    Values values();
    List<? extends Atom> atoms();
    long timestamp();
    List<Slice> slices();
    Slice slice(String name);
    Slice slice(int idx);

    /**
     * TODO JAVADOC
     */
    Slice add(Slice result);
    Slice remove(Slice result);
}
