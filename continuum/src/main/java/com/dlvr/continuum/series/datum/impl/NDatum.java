package com.dlvr.continuum.series.datum.impl;

import com.dlvr.continuum.series.datum.Datum;
import com.dlvr.continuum.series.datum.Fields;
import com.dlvr.continuum.series.datum.Tags;
import com.dlvr.continuum.series.db.ID;
import com.dlvr.continuum.series.db.impl.NDatumID;

/**
 * Base Datum implementation
 */
public class NDatum implements Datum {
    public String name;
    public NTags tags;
    public NFields fields;
    public double value;
    public Long timestamp;

    @Override
    public String name() {
        return name;
    }
    @Override
    public Tags tags() {
        return tags;
    }
    @Override
    public Fields fields() {
        return fields;
    }
    @Override
    public double value() {
        return value;
    }
    @Override
    public Long timestamp() {
        return timestamp;
    }

    @Override
    public ID ID() {
        // TODO: Cache? Mutability?
        return new NDatumID(this);
    }
}
