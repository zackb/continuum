package com.dlvr.continuum.db.query.impl;

import com.dlvr.continuum.db.query.Query;

/**
 * Query result collector factory and utils
 */
public class Collector {
    public static ICollector forQuery(Query query) {
        return new StatsCollector(query);
    }
}
