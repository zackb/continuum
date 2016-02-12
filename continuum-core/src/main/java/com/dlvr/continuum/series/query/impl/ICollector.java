package com.dlvr.continuum.series.query.impl;

import com.dlvr.continuum.series.db.Iterator;

/**
 * Created by zack on 2/11/16.
 */
public interface ICollector {
    void collect(Iterator iterator);
    double value();
}
