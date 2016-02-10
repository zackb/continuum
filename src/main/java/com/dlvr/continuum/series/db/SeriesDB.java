package com.dlvr.continuum.series.db;

import com.dlvr.continuum.series.point.Point;
import com.dlvr.continuum.series.query.Query;
import com.dlvr.continuum.series.query.QueryResult;

/**
 * Time series database
 */
public interface SeriesDB {

    /**
     * Write a datapoint to the time series database
     * @param point to write
     */
    void write(Point point) throws Exception;

    /**
     * Execute a query and return results
     * @return query result including aggregate functions, date ranges, and groupings if applicatble
     */
    QueryResult query(Query query) throws Exception;
}