package com.dlvr.continuum.series.query.impl;

import com.dlvr.continuum.series.datum.Fields;
import com.dlvr.continuum.series.datum.Tags;
import com.dlvr.continuum.series.query.Function;
import com.dlvr.continuum.series.query.Query;

import java.util.concurrent.TimeUnit;

/**
 * Base Query implementation
 */
public class BaseQuery implements Query {
    public String name;
    public Function function;
    public long start;
    public long end;
    public TimeUnit interval;
    public Tags tags;
    public Fields fields;

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
    public Function function() {
        return function;
    }

    @Override
    public long start() {
        return start;
    }

    @Override
    public long end() {
        return end;
    }

    @Override
    public TimeUnit interval() {
        return interval;
    }
}
