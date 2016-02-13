package com.dlvr.continuum.core.db.scan;

import com.dlvr.continuum.db.scan.Const;
import com.dlvr.continuum.db.scan.ScanResult;

import java.util.List;

/**
 * Tree structured results from a db scan
 */
public class NScanResult implements ScanResult {
    public String name;
    public double value;

    @Override
    public String name() {
        return name;
    }

    @Override
    public double value() {
        return value;
    }

    @Override
    public List<ScanResult> children() {
        return null;
    }

    @Override
    public Const.Type type() {
        return children().size() > 0 ? Const.Type.LEAF : Const.Type.NODE;
    }
}
