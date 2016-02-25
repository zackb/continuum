package com.dlvr.continuum.core.db.slice;

import com.dlvr.continuum.atom.Atom;
import com.dlvr.continuum.atom.Values;
import com.dlvr.continuum.db.slice.SliceResult;

import java.util.ArrayList;
import java.util.List;

/**
 * Tree structured results from a db slice
 */
public class NSliceResult implements SliceResult {

    public String name;
    public Values values;
    public long timestamp;
    public List<SliceResult> children;
    public List<Atom> atoms;

    @Override
    public String name() {
        return name;
    }

    @Override
    public Values values() {
        return values;
    }

    @Override
    public List<SliceResult> children() {
        return children;
    }

    @Override
    public List<Atom> atoms() {
        return atoms;
    }

    @Override
    public void addChild(SliceResult result) {
        if (children == null) children = new ArrayList<>();
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
}
