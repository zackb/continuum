package com.dlvr.continuum.control;

import com.dlvr.continuum.atom.Atom;
import com.dlvr.continuum.atom.AtomID;
import com.dlvr.continuum.slab.Translator;
import com.dlvr.continuum.slice.Scan;
import com.dlvr.continuum.slice.Scanner;
import com.dlvr.continuum.slice.Slice;

import static com.dlvr.continuum.Continuum.*;


/**
 * Central point of interaction with:
 *  - Slab
 *  - Slice
 *  - Scan
 *  - Translator
 * Created by zack on 3/16/16.
 */
public interface Controller {

    AtomBuilder atom();

    AtomBuilder atom(String name);

    ScanBuilder scan(String name);

    /**
     * Access the backing datastore for this continuum
     * @return Translator datastore engine
     */
    Translator<Atom> translator();

    /**
     * Write an atom to the time data store
     * @param atom to write or overwrite
     * @throws Exception error writing data
     */
    void write(Atom atom) throws Exception;

    /**
     * Retreive a single atom by ID from the datastore
     * @param id to retrieve for
     * @return atom for given ID or null if not exist
     * @throws Exception on underlying slabs failure
     */
    Atom get(AtomID id) throws Exception;

    /**
     * Query: Execute a set of operations on a scan of time from the datastore
     * Blocking
     * @param scan description of the view of the slice
     * @return Slice of atoms resulting from the scan
     *          includes: aggregate functions, date ranges, and groupings if applicatble
     * @throws Exception error reading or collecting atoms
     */
    Slice slice(Scan scan) throws Exception;

    Scanner scanner();
}
