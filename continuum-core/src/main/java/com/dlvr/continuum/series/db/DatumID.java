package com.dlvr.continuum.series.db;

import com.dlvr.continuum.series.datum.Tags;

/**
 * Created by zack on 2/11/16.
 */
public interface DatumID extends ID {
    String name();
    Tags tags();
    long timestamp();
}
