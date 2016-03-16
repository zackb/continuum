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
     * Database Iterator to iterate over time data
     * WARNING! The caller must call {#close()} on the slice to avoid resource leaks
     * @return database iterator
     */
    Iterator iterator();

    /**
     * Set the data store iterator to use for the next slice
     * @param iterator to use for this scanner
     */
    void iterator(Iterator iterator);

    /**
     * Set the dimension to scan on, space (name) or time (timestamp)
     * @param dimension for next scan to use
     */
    void dimension(Continuum.Dimension dimension);
}
