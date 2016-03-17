package com.dlvr.continuum.slab;

import com.dlvr.continuum.atom.AtomID;
import com.dlvr.continuum.atom.Values;

/**
 * Time and Space Scan Iterator
 * Created by zack on 2/11/16.
 */
public interface Iterator<T> extends AutoCloseable {

    AtomID ID();

    /**
     * Retreive the Atom with the ID of current()
     * @return unmarshalled atom
     */
    T get();

    /**
     * Retreive the values of the atom. This can save decoding time if the full Atom object is not needed
     * @return values
     */
    Values values();

    /**
     * Move iterator to first postion in the data store (oldest)
     */
    void seekToFirst();

    void seek(byte[] target);

    /**
     * Check if this iterator can work
     * @return true if iterator is valid and a call to next() will succeed
     */
    boolean valid();

    /**
     * Advance iterator position by one
     * @return true if iterator can be advanced further
     */
    boolean next();

    /**
     * Closes the iterator and releases underlying resources
     */
    void close();
}
