package continuum.core.slice;

import continuum.Continuum;
import continuum.slice.ScanID;
import continuum.atom.Fields;
import continuum.atom.Particles;
import continuum.slice.Collector;
import continuum.slice.Filter;
import continuum.slice.Function;
import continuum.slice.Scan;
import continuum.except.ZiggyStardustError;
import continuum.util.datetime.Interval;


/**
 * Base Scan implementation
 */
public class AScan implements Scan {

    public String name;
    public Function function;
    public long start;
    public long end;
    public Interval interval;
    public Particles particles;
    public Fields fields;
    public double value;
    public Collector collector;
    public Continuum.Dimension dimension;
    public String[] groups;
    public int limit;

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
    public Continuum.Dimension dimension() {
        return dimension;
    }

    public ScanID TimeID() {
        return new KScanID(end, name, particles);
    }

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
        return collector;
    }

    @Override
    public Filter filter() {
        return null;
    }

    @Override
    public int limit() {
        return limit;
    }
}
