package com.dlvr.continuum.db.slice;

import com.dlvr.continuum.atom.Atom;

import java.util.List;

/**
 * Result of space time slice
 */
public interface SliceResult {
    String name();
    Double value();
    List<SliceResult> children();
    List<Atom> atoms();
    void addChild(SliceResult result);
    SliceResult getChild(String name);
    SliceResult getChild(int idx);
    long timestamp();
    Const.Type type();
}
