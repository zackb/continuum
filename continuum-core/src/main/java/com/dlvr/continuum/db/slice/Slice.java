package com.dlvr.continuum.db.slice;

import com.dlvr.continuum.db.SliceID;
import com.dlvr.continuum.atom.Fields;
import com.dlvr.continuum.atom.Particles;

import java.util.concurrent.TimeUnit;

/**
 * Take a slice of space time
 */
public interface Slice {

    /**
     * Non-unique id for this slice
     */
    SliceID ID();

    /**
     * The measurement name to slice for
     * @return measurement name
     */
    String name();

    /**
     * Tag names and values to filter on. Or WILDCARD
     * Tags should be used for optimized queries
     * @return particles values to filter on
     */
    Particles particles();

    /**
     * Field names and values to filter on.
     * Fields should be used infrequently to filter on
     * @return field values to filter on
     */
    Fields fields();

    /**
     * Aggregation function for result's 'value' field.
     * If non-null, slice results are aggreagate values.
     * If null, slice results will have raw measurement values
     * @return aggregation function
     */
    Function function();

    /**
     * Timestamp to begin slice range (the smallest timestamp), expresed in millisecond epoch
     * @return unix epoch in milliseconds
     */
    long start();

    /**
     * Timestamp to end slice range (the largest timestamp), expresed in millisecond epoch
     * @return unix epoch in milliseconds
     */
    long end();

    /**
     * Date range interval to bucket results
     * If non-null, slice results are bucketed into date ranges using the {#getFunction()} aggreagate function
     * If null, slice results are atom-in-time values
     * @return time unit
     */
    TimeUnit interval();
    // TODODO: THIS HAS TO HAVE A DURATION!!!
}