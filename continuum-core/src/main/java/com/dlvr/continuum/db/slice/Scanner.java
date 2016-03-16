package com.dlvr.continuum.db.slice;

import com.dlvr.continuum.Continuum;
import com.dlvr.continuum.db.Iterator;

/**
 * Scan a data store to produce a slice
 * Created by zack on 3/15/16.
 */
public interface Scanner<T> {

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
     * Set the data store iterator to use for the next slice
     * @param iterator to use for this scanner
     */
    void iterator(Iterator iterator);

    void dimension(Continuum.Dimension dimension);
}
