package com.dlvr.continuum.db.scan;

import com.dlvr.continuum.db.Iterator;

/**
 * Scan filter engines
 * Created by zack on 2/12/16.
 */
public interface Filter {
    boolean filter(Iterator itr);
}
