package com.dlvr.continuum.core.db.scan;

import com.dlvr.continuum.db.ScanID;
import com.dlvr.continuum.atom.Fields;
import com.dlvr.continuum.atom.Particles;
import com.dlvr.continuum.core.db.SScanID;
import com.dlvr.continuum.db.scan.Function;
import com.dlvr.continuum.db.scan.Scan;

import java.util.concurrent.TimeUnit;

/**
 * Base Scan implementation
 */
public class NScan implements Scan {
    public String name;
    public Function function;
    public long start;
    public long end;
    public TimeUnit interval;
    public Particles particles;
    public Fields fields;
    public double value;

    @Override
    public ScanID ID() {
        //TODO:
        // Cache/Mutability?
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
    public TimeUnit interval() {
        return interval;
    }
}
