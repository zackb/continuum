package com.dlvr.continuum.core.datum;

import com.dlvr.continuum.datum.Datum;
import com.dlvr.continuum.datum.Fields;
import com.dlvr.continuum.datum.Tags;
import com.dlvr.continuum.db.DatumID;
import com.dlvr.continuum.core.db.SDatumID;

/**
 * Base Datum implementation
 */
public class NDatum implements Datum {
    public String name;
    public NTags tags;
    public Long timestamp;
    public NFields fields;
    public double value;

    public NDatum() { }

    public NDatum(String name, NTags tags, long timestamp, NFields fields, double value) {
        this.name = name;
        this.tags = tags;
        this.timestamp = timestamp;
        this.fields = fields;
        this.value = value;
    }

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
    public DatumID ID() {
        // TODO: Cache? Mutability?
        return new SDatumID(this);
    }
}
