package com.dlvr.continuum.core.db.scan;

import com.dlvr.continuum.db.scan.Collector;
import com.dlvr.continuum.db.scan.Scan;

/**
 * Scan atom collector factory and utils
 */
public class Collectors {
    public static Collector forScan(Scan scan) {
        return new StatsCollector(scan);
    }
}
