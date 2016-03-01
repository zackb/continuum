package com.dlvr.continuum.rest.client;

import com.dlvr.continuum.atom.Atom;
import com.dlvr.continuum.atom.Values;
import com.dlvr.continuum.core.atom.NValues;
import com.dlvr.continuum.core.atom.SAtom;
import com.dlvr.continuum.db.slice.SliceResult;

import java.util.List;

/**
 * POJO version of a slice result
 * Created by zack on 2/26/16.
 */
public class CSliceResult implements SliceResult {

    public String name;
    public NValues values;
    public List<SliceResult> children;
    public List<SAtom> atoms;
    public long timestamp;

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
    public List<? extends Atom> atoms() {
        return atoms;
    }

    @Override
    public void addChild(SliceResult sliceResult) { }

    @Override
    public SliceResult getChild(String s) {
        for (SliceResult res : children) {
            if (res.name().equals(name)) {
                return res;
            }
        }
        return null;
    }

    @Override
    public SliceResult getChild(int i) {
        return children.get(i);
    }

    @Override
    public long timestamp() {
        return timestamp;
    }
}
