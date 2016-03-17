package com.dlvr.continuum.core.slice;

import com.dlvr.continuum.atom.Atom;
import com.dlvr.continuum.slice.Filter;


/**
 * Space Filter by time range. Space is not ordered by timestamp so can MUST SKIP
 * Created by zack on 2/24/16.
 */
public class STimestampFilter implements Filter {

    private final long start;
    private final long end;

    public STimestampFilter(long start, long end) {
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
