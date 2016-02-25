package com.dlvr.continuum.core.db.slice;

import com.dlvr.continuum.atom.Atom;
import com.dlvr.continuum.db.slice.Filter;

/**
 * Filter by name for space or time dimension
 * Created by zack on 2/24/16.
 */
public class NameFilter implements Filter {

    private final String prefix;

    public NameFilter(String prefix) {
        this.prefix = prefix;
    }

    @Override
    public Action filter(Atom atom) {
        if (prefix == null) return Action.CONTINUE;
        // TODO: StartsWith or equals()?
        if (!atom.name().startsWith(prefix)) return Action.STOP;
        return Action.CONTINUE;
    }
}
