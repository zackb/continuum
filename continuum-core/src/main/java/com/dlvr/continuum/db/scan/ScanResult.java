package com.dlvr.continuum.db.scan;

import java.util.List;

/**
 * Result of space time scan
 */
public interface ScanResult {
    String name();
    double value();
    List<ScanResult> children();
    Const.Type type();
}
