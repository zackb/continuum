package com.dlvr.continuum.db.query.impl;

import com.dlvr.continuum.db.Iterator;

/**
 * Created by zack on 2/11/16.
 */
public interface ICollector {
    void collect(Iterator iterator);
    double value();
}
