package com.dlvr.continuum.db.scan;

import com.dlvr.continuum.db.ScanID;
import com.dlvr.continuum.atom.Fields;
import com.dlvr.continuum.atom.Particles;

import java.util.concurrent.TimeUnit;

/**
 * Scanning interface to space time db
 */
public interface Scan {

    /**
     * Non-unique id for this scan
     */
    ScanID ID();

    /**
     * The measurement name to scan for
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
     * If non-null, scan results are aggreagate values.
     * If null, scan results will have raw measurement values
     * @return aggregation function
     */
    Function function();

    /**
     * Timestamp to begin scan range (the smallest timestamp), expresed in millisecond epoch
     * @return unix epoch in milliseconds
     */
    long start();

    /**
     * Timestamp to end scan range (the largest timestamp), expresed in millisecond epoch
     * @return unix epoch in milliseconds
     */
    long end();

    /**
     * Date range interval to bucket results
     * If non-null, scan results are bucketed into date ranges using the {#getFunction()} aggreagate function
     * If null, scan results are atom-in-time values
     * @return time unit
     */
    TimeUnit interval();
}