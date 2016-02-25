package com.dlvr.continuum.db;

import com.dlvr.continuum.atom.Atom;

/**
 * Created by zack on 2/11/16.
 */
public interface Iterator {

    AtomID id();

    /**
     * Retreive the Atom with the ID of current()
     * @return unmarshalled atom
     */
    Atom get();

    /**
     * Retreive the value of the atom. This can save decoding time if the full Atom object is not needed
     * @return value
     */
    double value();

    void seekToFirst();

    void seek(byte[] target);

    void seekToLast();

    boolean hasNext();

    boolean next();

    boolean prev();

    /**
     * Closes the iterator and releases underlying resources
     */
    void close();
}
