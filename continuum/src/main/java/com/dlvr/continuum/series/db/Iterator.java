package com.dlvr.continuum.series.db;

import com.dlvr.continuum.series.datum.Datum;

/**
 * Created by zack on 2/11/16.
 */
public interface Iterator extends java.util.Iterator<ID> {
    /**
     * Retreive the Datum with the ID of current()
     * @return unmarshalled datum
     */
    Datum get();

    void seekToFirst();

    void seek(byte[] target);

    void seekToLast();

    /**
     * Closes the iterator and releases underlying resources
     */
    void close();
}
