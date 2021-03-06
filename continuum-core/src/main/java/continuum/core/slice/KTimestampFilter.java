package continuum.core.slice;

import continuum.atom.Atom;
import continuum.slice.Filter;

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
            return Action.SKIP;

        if (atom.timestamp() > start)
            return Action.SKIP;

        return Action.CONTINUE;
    }
}
