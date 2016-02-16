package com.dlvr.continuum.db;

import com.dlvr.continuum.atom.Atom;
import com.dlvr.continuum.db.slice.Slice;
import com.dlvr.continuum.db.slice.SliceResult;

/**
 * Time datastore
 */
public interface DB {

    /**
     * Write a datapoint to the time datastore
     * @param atom to write
     */
    void write(Atom atom) throws Exception;

    /**
     * Retreive a single atom by id from the datastore
     * @param id to retrieve for
     * @return atom for given id or null if not exist
     * @throws Exception on underlying slabs failure
     */
    Atom get(AtomID id) throws Exception;

    /**
     * Query: Execute a set of operations on a slice of time from the datastore
     * Blocking
     * @return slice result including aggregate functions, date ranges, and groupings if applicatble
     */
    SliceResult slice(Slice slice) throws Exception;

    /**
     * Database Iterator to iterate over time data
     * WARNING! The caller must call {#close()} on the result to avoid resource leaks
     * @return database iterator
     */
    Iterator iterator();

    /**
     * Attempt to flush and close the datastore and free all resources
     * @throws Exception
     */
    void close() throws Exception;
}