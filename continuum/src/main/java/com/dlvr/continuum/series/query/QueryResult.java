package com.dlvr.continuum.series.query;

import java.util.List;

/**
 * Result of time series query
 */
public interface QueryResult {
    String name();
    Double value();
    List<QueryResult> children();
    Const.Type type();
}
