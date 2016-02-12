package com.dlvr.continuum.series.db;

import com.dlvr.continuum.series.datum.Datum;

/**
 * Created by zack on 2/11/16.
 */
public interface Iterator {

    DatumID id();

    /**
     * Retreive the Datum with the ID of current()
     * @return unmarshalled datum
     */
    Datum get();

    /**
     * Retreive the value of the datum. This can save decoding time if the full Datum object is not needed
     * @return value
     */
    double value();

    void seekToFirst();

    void seek(byte[] target);

    void seekToLast();

    boolean hasNext();

    boolean next();

    /**
     * Closes the iterator and releases underlying resources
     */
    void close();
}
