package com.dlvr.continuum.series.datum.impl;

import com.dlvr.continuum.series.datum.Fields;
import com.dlvr.continuum.series.datum.Datum;
import com.dlvr.continuum.series.datum.Tags;

/**
 * Base Datum implementation
 */
public class BaseDatum implements Datum {
    public String name;
    public BaseTags tags;
    public BaseFields fields;
    public double value;
    public Long timestamp;

    private String id;

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
    public String ID() {
        if (id == null) {
            id = name() + "\\x00" + tags().ID() + "\\x00" + timestamp();
        }
        return id;
    }
}
