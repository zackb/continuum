package com.dlvr.continuum.core.db.slice;

import com.dlvr.continuum.db.SliceID;
import com.dlvr.continuum.atom.Fields;
import com.dlvr.continuum.atom.Particles;
import com.dlvr.continuum.core.db.SSliceID;
import com.dlvr.continuum.db.slice.Function;
import com.dlvr.continuum.db.slice.Slice;
import com.dlvr.continuum.util.datetime.Interval;


/**
 * Base Slice implementation
 */
public class NSlice implements Slice {
    public String name;
    public Function function;
    public long start;
    public long end;
    public Interval interval;
    public Particles particles;
    public Fields fields;
    public double value;

    @Override
    public SliceID ID() {
        //TODO:
        // Cache/Mutability?
        return new SSliceID(name, particles);
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public Particles particles() {
        return particles;
    }

    @Override
    public Fields fields() {
        return fields;
    }

    @Override
    public Function function() {
        return function;
    }

    @Override
    public long start() {
        return start;
    }

    @Override
    public long end() {
        return end;
    }

    @Override
    public Interval interval() {
        return interval;
    }
}
