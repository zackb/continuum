package com.dlvr.continuum.db.slice;

import com.dlvr.continuum.db.SliceID;
import com.dlvr.continuum.atom.Fields;
import com.dlvr.continuum.atom.Particles;
import com.dlvr.continuum.util.datetime.Interval;

/**
 * Take a slice of space time
 */
public interface Slice {

    /**
     * TODO: How to not need all three of these?
     * Non-unique id for this slice
     */
    SliceID ID();

    /**
     * ID for time key
     * @return id
     */
    SliceID TimeID();

    /**
     * ID for time series
     * @return id
     */
    SliceID SpaceID();

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
     * Aggregation function for result's 'values' field.
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
    Interval interval();

    /**
     * Result groupings on particles
     * @return particle names to group by
     */
    String[] groups();
}