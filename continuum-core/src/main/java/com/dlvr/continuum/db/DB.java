package com.dlvr.continuum.db;

import com.dlvr.continuum.atom.Atom;
import com.dlvr.continuum.db.slice.Scan;
import com.dlvr.continuum.db.slice.Slice;

/**
 * Time datastore
 */
public interface DB {

    /**
     * Write a datapoint to the time datastore
     * @param atom to write
     * @throws Exception error writing atom
     */
    void write(Atom atom) throws Exception;

    /**
     * Retreive a single atom by ID from the datastore
     * @param id to retrieve for
     * @return atom for given ID or null if not exist
     * @throws Exception on underlying slabs failure
     */
    Atom get(AtomID id) throws Exception;

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
     * WARNING! The caller must call {#close()} on the result to avoid resource leaks
     * @return database iterator
     */
    Iterator iterator();

    /**
     * Attempt to flush and close the datastore and free all resources
     * @throws Exception error closing datastore
     */
    void close() throws Exception;
}