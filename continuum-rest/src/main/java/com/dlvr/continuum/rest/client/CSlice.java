package com.dlvr.continuum.rest.client;

import com.dlvr.continuum.atom.Atom;
import com.dlvr.continuum.atom.Values;
import com.dlvr.continuum.core.atom.NValues;
import com.dlvr.continuum.core.atom.SAtom;
import com.dlvr.continuum.db.slice.Slice;

import java.util.List;

/**
 * POJO version of a slice result
 * Created by zack on 2/26/16.
 */
public class CSlice implements Slice {

    public String name;
    public NValues values;
    public List<Slice> children;
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
    public List<Slice> slices() {
        return children;
    }

    @Override
    public List<? extends Atom> atoms() {
        return atoms;
    }

    @Override
    public void addChild(Slice slice) { }

    @Override
    public Slice getChild(String name) {
        for (Slice slice: children) {
            if (slice.name().equals(name)) {
                return slice;
            }
        }
        return null;
    }

    @Override
    public Slice getChild(int i) {
        return children.get(i);
    }

    @Override
    public long timestamp() {
        return timestamp;
    }
}
