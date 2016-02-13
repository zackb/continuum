package com.dlvr.continuum.db.slice;

import com.dlvr.continuum.db.Iterator;

/**
 * Time slice atom collection and analyzation
 * Created by zack on 2/11/16.
 */
public interface Collector {
    void collect(Iterator iterator);
    double value();
}
