package com.dlvr.continuum.db;

import com.dlvr.continuum.atom.Tags;

/**
 * Created by zack on 2/11/16.
 */
public interface AtomID extends ID {
    String name();
    Tags tags();
    long timestamp();
}
