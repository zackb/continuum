package com.dlvr.continuum.rest.client;

import com.dlvr.continuum.atom.Atom;
import com.dlvr.continuum.atom.Values;
import com.dlvr.continuum.core.atom.NValues;
import com.dlvr.continuum.core.atom.SAtom;
import com.dlvr.continuum.db.slice.Slice;

import java.util.ArrayList;
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
    public Slice add(Slice slice) {
        if (children == null) children = new ArrayList<>();
        children.add(slice);
        return slice;
    }

    @Override
    public Slice remove(Slice slice) {
        if (children != null)
            children.remove(slice);
        return slice;
    }

    @Override
    public Slice slice(String name) {

        if (children == null) return null;

        for (Slice slice : children)
            if (slice.name().equals(name))
                return slice;

        return null;
    }

    @Override
    public Slice slice(int i) {
        return children.get(i);
    }

    @Override
    public long timestamp() {
        return timestamp;
    }
}
