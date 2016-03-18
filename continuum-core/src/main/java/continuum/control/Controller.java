package continuum.control;

import continuum.atom.Atom;
import continuum.atom.AtomID;
import continuum.slab.Translator;
import continuum.slice.Scan;
import continuum.slice.Scanner;
import continuum.slice.Slice;

import java.util.stream.Stream;

import static continuum.Continuum.*;


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

    /**
     * Continuously scan the continuum for new data
     * @param scan to run on new data
     * @return stream of slices resulting from scans
     */
    Stream<Slice> stream(Scan scan) throws Exception;

    Scanner scanner();
}
