package com.dlvr.continuum.series.query.impl;

import com.dlvr.continuum.series.query.Query;

/**
 * Query result collector factory and utils
 */
public class Collector {
    public static ICollector forQuery(Query query) {
        return new StatsCollector(query);
    }
}
