package com.dlvr.continuum.db.query;

import com.dlvr.continuum.db.Iterator;

/**
 * Query filter engines
 * Created by zack on 2/12/16.
 */
public interface Filter {
    boolean filter(Iterator itr);
}
