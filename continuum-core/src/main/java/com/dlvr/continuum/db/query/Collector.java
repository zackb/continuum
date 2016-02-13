package com.dlvr.continuum.db.query;

import com.dlvr.continuum.db.Iterator;

/**
 * Scan result collection and analyzation
 * Created by zack on 2/11/16.
 */
public interface Collector {
    void collect(Iterator iterator);
    double value();
}
