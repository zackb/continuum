package com.dlvr.continuum.core.db.slice;

import com.dlvr.continuum.Continuum;
import com.dlvr.continuum.core.db.KSliceID;
import com.dlvr.continuum.db.SliceID;
import com.dlvr.continuum.atom.Fields;
import com.dlvr.continuum.atom.Particles;
import com.dlvr.continuum.core.db.SSliceID;
import com.dlvr.continuum.db.slice.Collector;
import com.dlvr.continuum.db.slice.Function;
import com.dlvr.continuum.db.slice.Slice;
import com.dlvr.continuum.except.ZiggyStardustError;
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
    public Collector[] collectors;
    public Continuum.Dimension dimension;
    public String[] groups;

    @Override
    public SliceID ID() {
        //TODO:
        // Cache/Mutability?
        if (dimension == Continuum.Dimension.SPACE)
            return SpaceID();
        else if (dimension == Continuum.Dimension.TIME)
            return TimeID();

        throw new ZiggyStardustError();
    }

    @Override
    public SliceID TimeID() {
        return new KSliceID(start, name, particles);
    }

    @Override
    public SliceID SpaceID() {
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

    @Override
    public String[] groups() {
        return groups;
    }
}
