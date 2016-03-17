package com.dlvr.continuum.slice;

import com.dlvr.continuum.atom.Atom;
import com.dlvr.continuum.slab.Iterator;

/**
 * Scan a data store to produce a slice
 * Created by zack on 3/15/16.
 */
public interface Scanner {

    /**
     * Query: Execute a set of operations on a scan of time from the datastore
     * Blocking
     * @param scan description of the view of the slice
     * @return Slice of atoms resulting from the scan
     *          includes: aggregate functions, date ranges, and groupings if applicatble
     * @throws Exception error reading or collecting atoms
     */
    Slice slice(Scan scan) throws Exception;

    /**
     * Set the slab storage iterator
     * @param iterator to use for this scanner
     */
    void iterator(Iterator<Atom> iterator);
}
