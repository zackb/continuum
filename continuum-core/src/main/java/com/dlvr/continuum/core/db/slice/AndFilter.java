package com.dlvr.continuum.core.db.slice;

import com.dlvr.continuum.atom.Atom;
import com.dlvr.continuum.db.slice.Filter;

/**
 * Combine multiple filters
 * Created by zack on 2/24/16.
 */
public class AndFilter implements Filter {
    private final Filter[] filters;
    public AndFilter(Filter... filters) {
        this.filters = filters;
    }
    @Override
    public Action filter(Atom atom) {
        for (Filter filter : filters) {
            Action a = filter.filter(atom);
            if (a == Action.STOP)
                return Action.STOP;
            if (a == Action.SKIP)
                return Action.SKIP;
        }
        return Action.CONTINUE;
    }
}
