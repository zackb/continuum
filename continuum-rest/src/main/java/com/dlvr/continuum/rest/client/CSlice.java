package com.dlvr.continuum.rest.client;

import com.dlvr.continuum.atom.Atom;
import com.dlvr.continuum.atom.Values;
import com.dlvr.continuum.core.atom.NValues;
import com.dlvr.continuum.core.atom.SAtom;
import com.dlvr.continuum.slice.Slice;

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

    /**
     * {@inheritDoc}
     */
    @Override
    public String name() {
        return name;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Values values() {
        return values;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Slice> slices() {
        return children;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<? extends Atom> atoms() {
        return atoms;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Slice add(Slice slice) {
        if (children == null) children = new ArrayList<>();
        children.add(slice);
        return slice;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Slice remove(Slice slice) {
        if (children != null)
            children.remove(slice);
        return slice;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Slice slice(String name) {

        if (children == null) return null;

        for (Slice slice : children)
            if (slice.name().equals(name))
                return slice;

        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Slice slice(int i) {
        return children.get(i);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long timestamp() {
        return timestamp;
    }
}
