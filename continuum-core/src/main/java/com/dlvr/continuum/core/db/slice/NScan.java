package com.dlvr.continuum.core.db.slice;

import com.dlvr.continuum.Continuum;
import com.dlvr.continuum.core.db.KScanID;
import com.dlvr.continuum.core.db.SScanID;
import com.dlvr.continuum.db.ScanID;
import com.dlvr.continuum.atom.Fields;
import com.dlvr.continuum.atom.Particles;
import com.dlvr.continuum.db.slice.Collector;
import com.dlvr.continuum.db.slice.Filter;
import com.dlvr.continuum.db.slice.Function;
import com.dlvr.continuum.db.slice.Scan;
import com.dlvr.continuum.except.ZiggyStardustError;
import com.dlvr.continuum.util.datetime.Interval;


/**
 * Base Scan implementation
 */
public class NScan implements Scan {

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
    public ScanID ID() {
        //TODO:
        // Cache/Mutability?
        if (dimension == Continuum.Dimension.SPACE)
            return SpaceID();
        else if (dimension == Continuum.Dimension.TIME)
            return TimeID();

        throw new ZiggyStardustError();
    }

    @Override
    public ScanID TimeID() {
        return new KScanID(start, name, particles);
    }

    @Override
    public ScanID SpaceID() {
        return new SScanID(name, particles);
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

    @Override
    public Collector collector() {
        return null;
    }

    @Override
    public Filter filter() {
        return null;
    }
}