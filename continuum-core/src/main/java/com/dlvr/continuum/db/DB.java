package com.dlvr.continuum.db;

import com.dlvr.continuum.atom.Atom;

/**
 * Time datastore
 */
public interface DB {

    /**
     * Open the datastore
     * @throws Exception opening underlying slab resource
     */
    void open() throws Exception;

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
    Atom read(AtomID id) throws Exception;

    /**
     * The underlying slab storage resource for this datastore
     * @return this slab storage
     */
    Slab slab();

    /**
     * Attempt to flush and close the datastore and free all resources
     * @throws Exception error closing datastore
     */
    void close() throws Exception;
}