package com.dlvr.continuum.db;

import com.dlvr.continuum.atom.Atom;
import com.dlvr.continuum.db.slice.Slice;
import com.dlvr.continuum.db.slice.SliceResult;

/**
 * Time db datastore
 */
public interface DB {

    /**
     * Write a datapoint to the time db database
     * @param atom to write
     */
    void write(Atom atom) throws Exception;

    /**
     * Retreive a single atom by id
     * @param id to retrieve for
     * @return atom for given id or null if not exist
     * @throws Exception on underlying slabs failure
     */
    Atom get(AtomID id) throws Exception;

    /**
     * Execute a slice and return results
     * @return slice result including aggregate functions, date ranges, and groupings if applicatble
     */
    SliceResult slice(Slice slice) throws Exception;

    /**
     * Database Iterator
     * WARN: The caller must call {#close()} on the result to avoid resource leaks
     * @return database iterator
     */
    Iterator iterator();

    /**
     * Attempt to flush and close the data store and free all resources
     * @throws Exception
     */
    void close() throws Exception;
}