package com.dlvr.continuum.db.slice;

import com.dlvr.continuum.atom.Atom;

/**
 * Time scan atom collection and analyzation
 * Created by zack on 2/11/16.
 */
public interface Collector {
    void collect(Atom atom);
    String name();
    Slice result();
}
