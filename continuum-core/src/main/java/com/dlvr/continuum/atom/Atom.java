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
     * The tags associated with this time db data atom.
     * Under the hood tags.size() * name time db are created
     * Only use tags for data which needs to be queried scalably
     */
    Tags tags();

    /**
     * Fields associated with this tagged, measurement's data atom
     * Use tags for meta data which does not need to be queried scalably
     */
    Fields fields();

    /**
     * Non-empty double value of this data atom
     */
    double value();

    /**
     * The timestamp of the atom expressed as millisecond epoch
     */
    Long timestamp();
}
