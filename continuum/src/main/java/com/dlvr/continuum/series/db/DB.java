package com.dlvr.continuum.series.db;

import com.dlvr.continuum.series.datum.Datum;
import com.dlvr.continuum.series.query.Query;
import com.dlvr.continuum.series.query.QueryResult;

/**
 * Time series datastore
 */
public interface DB {

    /**
     * Write a datapoint to the time series database
     * @param datum to write
     */
    void write(Datum datum) throws Exception;

    /**
     * Execute a query and return results
     * @return query result including aggregate functions, date ranges, and groupings if applicatble
     */
    QueryResult query(Query query) throws Exception;

    /**
     * Database Iterator
     * WARN: The caller must call {#close()} on the result to avoid resource leaks
     * @return database iterator
     */
    Iterator iterator();

    /**
     * Attempt to flush and close the data store and free all resources
     * @throws Exception
     */
    void close() throws Exception;
}