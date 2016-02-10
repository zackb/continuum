package com.dlvr.continuum.series.db;

/**
 * Big chunk of data
 */
public interface Slab {
    /**
     * Get some data by key
     * @param key id of data
     * @return bytes of data
     */
    byte[] get(String key) throws Exception;

    /**
     * Store some data by key
     * @param key to store, duplicates' will be push()'d
     * @param value some bytes to store
     */
    void put(String key, byte[] value) throws Exception;

    /**
     * Store some data by key and push duplicate values instead of overwriting
     * @param key to store, duplicates' will be push()'d
     * @param value some bytes to store
     */
    void merge(String key, byte[] value) throws Exception;

    /**
     * Close the data store and free all resources
     */
    void close();
}
