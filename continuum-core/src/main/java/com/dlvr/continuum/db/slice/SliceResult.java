package com.dlvr.continuum.db.slice;

import java.util.List;

/**
 * Result of space time slice
 */
public interface SliceResult {
    String name();
    Double value();
    List<SliceResult> children();
    void addChild(SliceResult result);
    SliceResult getChild(String name);
    SliceResult getChild(int idx);
    long timestamp();
    Const.Type type();
}
