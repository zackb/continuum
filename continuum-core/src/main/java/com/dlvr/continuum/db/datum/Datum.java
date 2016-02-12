package com.dlvr.continuum.db.datum;

import com.dlvr.continuum.db.DatumID;

import java.io.Serializable;

/**
 * Time db data datum
 */
public interface Datum extends Serializable {

    /**
     *
     */
    DatumID ID();

    /**
     * Measurement name
     */
    String name();

    /**
     * The tags associated with this time db data datum.
     * Under the hood tags.size() * name time db are created
     * Only use tags for data which needs to be queried scalably
     */
    Tags tags();

    /**
     * Fields associated with this tagged, measurement's data datum
     * Use tags for meta data which does not need to be queried scalably
     */
    Fields fields();

    /**
     * Non-empty double value of this data datum
     */
    double value();

    /**
     * The timestamp of the datum expressed as millisecond epoch
     */
    Long timestamp();
}
