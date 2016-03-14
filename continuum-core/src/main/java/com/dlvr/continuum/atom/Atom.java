package com.dlvr.continuum.atom;

import com.dlvr.continuum.db.AtomID;

import java.io.Serializable;

/**
 * Time db data atom
 */
public interface Atom extends Serializable {

    /**
     * ID of this atom in the space time data store.
     * Format is optimized per Dimension
     * @return unique ID in this dimenstion
     */
    AtomID ID();

    /**
     * Measurement name
     * @return name of the atom
     */
    String name();

    /**
     * The particles associated with this time db data atom.
     * Under the hood particles.size() * name time db are created
     * Only use particles for data which needs to be queried scalably
     * @return particles of the atomg
     */
    Particles particles();

    /**
     * Fields associated with this tagged, measurement's data atom
     * Use particles for meta data which does not need to be queried scalably
     * @return fields of the atom
     */
    Fields fields();

    /**
     * Values tuple of (min,max,count,sum,values)
     * @return values of the atom
     */
    Values values();

    /**
     * The timestamp of the atom expressed as millisecond epoch
     * @return timestamp of the atom
     */
    Long timestamp();
}
