package com.dlvr.continuum.db;

/**
 * Slab storage responsible for a big chunk of data
 */
public interface Slab {

    /**
     * Open underlying resources for this slab storage resource
     * @throws Exception
     */
    void open() throws Exception;

    /**
     * Get some data by key
     * @param key ID of data
     * @throws Exception error reading from slab
     * @return bytes of data
     */
    byte[] read(byte[] key) throws Exception;

    /**
     * Store some data by key
     * @param key to store, duplicates' will be push()'d
     * @param value some bytes to store
     * @throws Exception error writing to slab
     */
    void write(byte[] key, byte[] value) throws Exception;

    /**
     * Store some data by key and push duplicate values instead of overwriting
     * @param key to store, duplicates' will be push()'d
     * @param value some bytes to store
     * @throws Exception error merging slab
     */
    void merge(byte[] key, byte[] value) throws Exception;

    /**
     * Close the data store and free all resources
     * @throws Exception error closing slab
     */
    void close() throws Exception;
}
