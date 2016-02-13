package com.dlvr.continuum.db;

import com.dlvr.continuum.datum.Datum;
import com.dlvr.continuum.db.query.Query;
import com.dlvr.continuum.db.query.QueryResult;

/**
 * Time db datastore
 */
public interface DB {

    /**
     * Write a datapoint to the time db database
     * @param datum to write
     */
    void write(Datum datum) throws Exception;

    /**
     * Retreive a single datum by id
     * @param id to retrieve for
     * @return datum for given id or null if not exist
     * @throws Exception on underlying slabs failure
     */
    Datum get(DatumID id) throws Exception;

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