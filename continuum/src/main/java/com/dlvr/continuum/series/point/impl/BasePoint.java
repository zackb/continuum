package com.dlvr.continuum.series.point.impl;

import com.dlvr.continuum.series.point.Fields;
import com.dlvr.continuum.series.point.Point;
import com.dlvr.continuum.series.point.Tags;

/**
 * Base Point implementation
 */
public class BasePoint implements Point {
    public String name;
    public Tags tags;
    public Fields fields;
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
}
