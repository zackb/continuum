package com.dlvr.continuum.core.db.slice;

import com.dlvr.continuum.db.slice.Filter;
import com.dlvr.continuum.db.slice.Slice;

/**
 * Filters engine, factory, and utils
 * Created by zack on 2/12/16.
 */
public class Filters {
    public static Filter forSlice(Slice slice) {
        return new ParticlesFilter(slice);
    }
}
