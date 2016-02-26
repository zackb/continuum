package com.dlvr.continuum.db.slice;

import com.dlvr.continuum.atom.Atom;

/**
 * Collect full deserialized atoms in addition to the ID
 * Created by zack on 2/25/16.
 */
public interface BodyCollector extends Collector {
    void collect(Atom atom);
}
