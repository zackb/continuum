package com.dlvr.continuum.db.slice;

import com.dlvr.continuum.db.Iterator;

/**
 * Time slice filter engines
 * Created by zack on 2/12/16.
 */
public interface Filter {
    boolean filter(Iterator itr);
}
