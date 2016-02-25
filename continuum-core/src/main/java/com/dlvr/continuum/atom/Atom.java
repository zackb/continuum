package com.dlvr.continuum.atom;

import com.dlvr.continuum.db.AtomID;

import java.io.Serializable;

/**
 * Time db data atom
 */
public interface Atom extends Serializable {

    /**
     *
     */
    AtomID ID();

    /**
     * Measurement name
     */
    String name();

    /**
     * The particles associated with this time db data atom.
     * Under the hood particles.size() * name time db are created
     * Only use particles for data which needs to be queried scalably
     */
    Particles particles();

    /**
     * Fields associated with this tagged, measurement's data atom
     * Use particles for meta data which does not need to be queried scalably
     */
    Fields fields();

    /**
     * Values tuple of (min,max,count,sum,values)
     */
    Values values();

    /**
     * The timestamp of the atom expressed as millisecond epoch
     */
    Long timestamp();
}
