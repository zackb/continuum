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
    List<Slice> slices();
    List<? extends Atom> atoms();
    Slice add(Slice result);
    Slice remove(Slice result);
    Slice child(String name);
    Slice child(int idx);
    List<Slice> children();
    long timestamp();
}
