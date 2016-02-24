package com.dlvr.continuum.core.db.slice;

import com.dlvr.continuum.atom.Atom;
import com.dlvr.continuum.db.slice.Filter;
import com.dlvr.continuum.db.slice.Slice;

/**
 * Filter non-matching partices in slices
 * Created by zack on 2/23/16.
 */
public class ParticlesFilter implements Filter {

    private final Slice slice;

    public ParticlesFilter(Slice slice) {
        this.slice = slice;
    }

    @Override
    public Action filter(Atom atom) {
        if (slice.particles() == null) return Action.CONTINUE;

        for (String name : slice.particles().keySet()) {
            if (!atom.particles().get(name).equals(slice.particles().get(name))) {
                return Action.STOP;
            }
        }

        if (atom.timestamp() < slice.end())
            return Action.STOP;

        if (atom.timestamp() > slice.start())
            return Action.STOP;

        return Action.CONTINUE;
    }
}
