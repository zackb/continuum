package com.dlvr.continuum.db.slice;

import java.util.List;
import java.util.Map;

/**
 * Result of space time slice
 */
public interface SliceResult {
    String name();
    Map<Long, Double> value();
    List<SliceResult> children();
    Const.Type type();
}
