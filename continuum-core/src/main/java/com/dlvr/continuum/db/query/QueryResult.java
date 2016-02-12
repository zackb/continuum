package com.dlvr.continuum.db.query;

import java.util.List;

/**
 * Result of time db query
 */
public interface QueryResult {
    String name();
    double value();
    List<QueryResult> children();
    Const.Type type();
}
