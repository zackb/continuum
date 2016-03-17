package com.dlvr.continuum.db;

/**
 * Serde. Time data slab marshaller / demarshaller.
 */
public interface Translator<T> {

    /**
     * Write a datapoint to the time datastore
     * @param t atom to write
     * @throws Exception error writing atom
     */
    void write(T t) throws Exception;

    /**
     * Retreive a single atom by ID from the datastore
     * @param id to retrieve for
     * @return atom for given ID or null if not exist
     * @throws Exception on underlying slabs failure
     */
    T read(AtomID id) throws Exception;

    /**
     * Get an iterator for the data store.
     * WARN Caller MUST Call close()
     * Available exclusively to caller until close() is called.
     * @return new iterator
     */
    Iterator<T> iterator();

    /**
     * The underlying slab storage resource for this translator
     * @return this slab storage
     */
    Slab slab();
}