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
    void addChild(Slice result);
    Slice getChild(String name);
    Slice getChild(int idx);
    long timestamp();
}
