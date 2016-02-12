package com.dlvr.continuum.series.query;

import com.dlvr.continuum.series.datum.Fields;
import com.dlvr.continuum.series.datum.Tags;

import java.util.concurrent.TimeUnit;

/**
 * Query interface to time series
 */
public interface Query {

    /**
     * The measurement name to scan for
     * @return measurement name
     */
    String name();

    /**
     * Tag names and values to filter on. Or WILDCARD
     * Tags should be used for optimized queries
     * @return tags values to filter on
     */
    Tags tags();

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
     * If null, query results are datum-in-time values
     * @return time unit
     */
    TimeUnit interval();
}