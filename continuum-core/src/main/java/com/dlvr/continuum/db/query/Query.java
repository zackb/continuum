package com.dlvr.continuum.db.query;

import com.dlvr.continuum.db.QueryID;
import com.dlvr.continuum.atom.Fields;
import com.dlvr.continuum.atom.Particles;

import java.util.concurrent.TimeUnit;

/**
 * Query interface to time db
 */
public interface Query {

    /**
     * Non-unique id for this query
     */
    QueryID ID();

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
     * If non-null, query results are aggreagate values.
     * If null, query results will have raw measurement values
     * @return aggregation function
     */
    Function function();

    /**
     * Timestamp to begin query range (the smallest timestamp), expresed in millisecond epoch
     * @return unix epoch in milliseconds
     */
    long start();

    /**
     * Timestamp to end query range (the largest timestamp), expresed in millisecond epoch
     * @return unix epoch in milliseconds
     */
    long end();

    /**
     * Date range interval to bucket results
     * If non-null, query results are bucketed into date ranges using the {#getFunction()} aggreagate function
     * If null, query results are atom-in-time values
     * @return time unit
     */
    TimeUnit interval();
}