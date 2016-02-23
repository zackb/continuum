package com.dlvr.continuum.core.db.slice;

import com.dlvr.continuum.db.slice.Const;
import com.dlvr.continuum.db.slice.SliceResult;

import java.util.ArrayList;
import java.util.List;

/**
 * Tree structured results from a db slice
 */
public class NSliceResult implements SliceResult {

    public String name;
    public Double value;
    public long timestamp;
    public List<SliceResult> children = new ArrayList<>();

    @Override
    public String name() {
        return name;
    }

    @Override
    public Double value() {
        return value;
    }

    @Override
    public List<SliceResult> children() {
        return children;
    }

    @Override
    public void addChild(SliceResult result) {
        children.add(result);
    }

    @Override
    public SliceResult getChild(String name) {
        for (SliceResult res : children) {
            if (res.name().equals(name)) {
                return res;
            }
        }
        return null;
    }

    @Override
    public SliceResult getChild(int idx) {
        return children.get(idx);
    }

    @Override
    public long timestamp() {
        return timestamp;
    }

    @Override
    public Const.Type type() {
        return children().size() > 0 ? Const.Type.LEAF : Const.Type.NODE;
    }
}
