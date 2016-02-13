package com.dlvr.continuum.core.db.query;

import com.dlvr.continuum.db.query.Collector;
import com.dlvr.continuum.db.query.Query;

/**
 * Query result collector factory and utils
 */
public class Collectors {
    public static Collector forQuery(Query query) {
        return new StatsCollector(query);
    }
}
