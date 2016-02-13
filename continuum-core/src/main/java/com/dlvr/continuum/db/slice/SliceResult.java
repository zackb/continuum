package com.dlvr.continuum.db.slice;

import java.util.List;

/**
 * Result of space time slice
 */
public interface SliceResult {
    String name();
    double value();
    List<SliceResult> children();
    Const.Type type();
}
