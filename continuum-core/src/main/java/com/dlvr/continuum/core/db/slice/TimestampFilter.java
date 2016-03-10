package com.dlvr.continuum.core.db.slice;

import com.dlvr.continuum.atom.Atom;
import com.dlvr.continuum.db.slice.Filter;


/**
 * Filter by time range
 * Created by zack on 2/24/16.
 */
public class TimestampFilter implements Filter {

    private final long start;
    private final long end;

    public TimestampFilter(long start, long end) {
        this.start = start;
        this.end = end;
    }

    @Override
    public Action filter(Atom atom) {

        if (atom.timestamp() < end)
            return Action.SKIP;

        if (atom.timestamp() > start)
            return Action.SKIP;

        return Action.CONTINUE;
    }
}
