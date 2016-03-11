package com.dlvr.continuum.core.db.slice;

import com.dlvr.continuum.atom.Atom;
import com.dlvr.continuum.db.slice.Filter;

/**
 * Time Filter by time range. Time is ordered by timestamp so can STOP
 * Created by zack on 3/10/16.
 */
public class KTimestampFilter implements Filter {

    private final long start;
    private final long end;

    public KTimestampFilter(long start, long end) {
        this.start = start;
        this.end = end;
    }

    @Override
    public Action filter(Atom atom) {

        if (atom.timestamp() < end)
            return Action.STOP;

        if (atom.timestamp() > start)
            return Action.STOP;

        return Action.CONTINUE;
    }
}
