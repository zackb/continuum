package com.dlvr.continuum.db.slice;

import com.dlvr.continuum.atom.Atom;
import com.dlvr.continuum.atom.Values;

import java.util.List;

/**
 * Result of space time slice
 */
public interface SliceResult {
    String name();
    Values values();
    List<SliceResult> children();
    List<? extends Atom> atoms();
    void addChild(SliceResult result);
    SliceResult getChild(String name);
    SliceResult getChild(int idx);
    long timestamp();
}
