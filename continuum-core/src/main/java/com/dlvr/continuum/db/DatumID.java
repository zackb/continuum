package com.dlvr.continuum.db;

import com.dlvr.continuum.db.datum.Tags;

/**
 * Created by zack on 2/11/16.
 */
public interface DatumID extends ID {
    String name();
    Tags tags();
    long timestamp();
}
