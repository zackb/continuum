package com.dlvr.continuum.core.db.slice;

import com.dlvr.continuum.Continuum;
import com.dlvr.continuum.atom.Particles;
import com.dlvr.continuum.db.slice.Filter;
import com.dlvr.continuum.db.slice.Scan;
import com.dlvr.continuum.except.ZiggyStardustError;

/**
 * Filters engine, factory, and utils
 * Created by zack on 2/12/16.
 */
public class Filters {
    public static Filter forSlice(Scan scan) {
        return and(
            timestamp(Continuum.Dimension.SPACE /*WART!*/, scan.start(), scan.end()),
            name(scan.name()),
            particles(scan.particles())
        );
    }

    /**
     * Filter on atom particle names and values
     * @param particles to filter on
     * @return new particles filter
     */
    public static ParticlesFilter particles(Particles particles) {
        return new ParticlesFilter(particles);
    }

    /**
     * Filter on atom name
     * @param name atom name
     * @return new name filter
     */
    public static NameFilter name(String name) {
        return new NameFilter(name);
    }

    /**
     * Filter on atom timestamp
     * @param dimension of this continuum
     * @param start epoch millis to begin slicing, largest time
     * @param end epoch millis to stop slicing, smallest time
     * @return new timestamp filter
     */
    public static Filter timestamp(Continuum.Dimension dimension, long start, long end) {
        if (dimension == Continuum.Dimension.SPACE)
            return new STimestampFilter(start, end);
        else if (dimension == Continuum.Dimension.TIME)
            return new KTimestampFilter(start, end);
        throw new ZiggyStardustError();
    }

    /**
     * Combine filters using AND operator
     * @param filters to combine
     * @return new and filter
     */
    public static AndFilter and(Filter... filters) {
        return new AndFilter(filters);
    }
}
