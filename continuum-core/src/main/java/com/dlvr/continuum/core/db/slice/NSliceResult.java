package com.dlvr.continuum.core.db.slice;

import com.dlvr.continuum.db.slice.Const;
import com.dlvr.continuum.db.slice.SliceResult;

import java.util.List;
import java.util.Map;

/**
 * Tree structured results from a db slice
 */
public class NSliceResult implements SliceResult {
    public String name;
    public Map<Long, Double> value;

    @Override
    public String name() {
        return name;
    }

    @Override
    public Map<Long, Double> value() {
        return value;
    }

    @Override
    public List<SliceResult> children() {
        return null;
    }

    @Override
    public Const.Type type() {
        return children().size() > 0 ? Const.Type.LEAF : Const.Type.NODE;
    }
}
